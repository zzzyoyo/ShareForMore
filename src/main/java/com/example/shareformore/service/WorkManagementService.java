package com.example.shareformore.service;

import com.example.shareformore.dto.TagDto;
import com.example.shareformore.dto.WorkDetailDto;
import com.example.shareformore.dto.WorkDto;
import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;
import com.example.shareformore.exception.column.ColumnNotFoundException;
import com.example.shareformore.exception.tag.TagNotFoundException;
import com.example.shareformore.exception.user.UserNotFoundException;
import com.example.shareformore.exception.work.*;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import com.example.shareformore.response.ResponseHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkManagementService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final ColumnRepository columnRepository;
    private final TagRepository tagRepository;

    final private Set<String> allowSuffix = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif"));
    private static String ACCESS_URL;
    private static String UPLOAD_PATH;

    Logger logger = LoggerFactory.getLogger(WorkManagementService.class);

    /**
     * 所有的exception都交给ControllerAdvisor来处理
     */

    @Autowired
    public WorkManagementService(UserRepository userRepository, WorkRepository workRepository, ColumnRepository columnRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.columnRepository = columnRepository;
        this.tagRepository = tagRepository;
    }

    /**
     * 上传作品
     */

    public ResponseHolder uploadWork(String author, Long columnId, List<Long> tagList, String title, String description,
                                     String content, int price, MultipartFile image) throws IOException {
        User user = userRepository.findByName(author);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(author);
        }

        if ((content == null || content.isBlank()) && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        SpecialColumn column = null;
        if (columnId != null) {
            column = columnRepository.findByColumnId(columnId);
            if (column == null && columnId >= 0) {
                logger.debug("column not found error");
                throw new ColumnNotFoundException(columnId);
            }
        }

        Set<Tag> tagSet = null;
        if (tagList != null) {
            tagSet = getTagSet(tagList);
        }

        Work work = new Work(user, column, title, description, content, image.getBytes(), price, tagSet);
        workRepository.save(work);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder updateWork(String author, Long workId, Long columnId, List<Long> tagList, String title,
                                     String description, String content, int price, MultipartFile image) throws IOException {
        User user = userRepository.findByName(author);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(author);
        }

        if ((content == null || content.isBlank()) && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        Work work = workRepository.findByWorkId(workId);

        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(workId);
        }

        SpecialColumn column = null;
        if (columnId != null) {
            column = columnRepository.findByColumnId(columnId);
            if (column == null && columnId >= 0) {
                logger.debug("column not found error");
                throw new ColumnNotFoundException(columnId);
            }
        }

        if (!user.equals(work.getAuthor())) {
            logger.debug("illegal update work error");
            throw new IllegalUpdateWorkException(author, workId);
        }


        Set<Tag> tagSet = null;
        if (tagList != null) {
            tagSet = getTagSet(tagList);
        }
        work.setColumn(column);
        work.setTitle(title);
        work.setDescription(description);
        work.setContent(content);
        work.setImage(image.getBytes());
        work.setTagSet(tagSet);
        work.setPrice(price);
        work.setUpdateTime(new Timestamp(new Date().getTime()));
        workRepository.save(work);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder payWork(String username, Long work_id) {
        User user = userRepository.findByName(username);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(username);
        }

        Work work = workRepository.findByWorkId(work_id);
        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(work_id);
        }

        // 作者不能买自己的作品
        if (work.getAuthor().equals(user)) {
            logger.debug("author buy work error");
            throw new AuthorBuyWorkException();
        }

        // 不能买已经买过的作品
        if (work.getPurchase().contains(user)) {
            logger.debug("work has been bought error");
            throw new WorkHasBeenBoughtException();
        }

        int credit = user.getBalance();
        if (credit < work.getPrice()) {
            throw new InsufficientBalanceException(work.getPrice());
        }
        user.payWork(work);
        userRepository.save(user);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    private Set<Tag> getTagSet(List<Long> tag_list) {
        Set<Tag> tagSet = new HashSet<>();
        for (Long tag_id : tag_list) {
            Tag temp_tag = tagRepository.findByTagId(tag_id);
            if (temp_tag == null) {
                logger.debug("tag not found error");
                throw new TagNotFoundException(tag_id);
            }
            tagSet.add(temp_tag);
        }
        return tagSet;
    }

    public ResponseHolder listWork(String title, String tag, String name, Long columnId) {
        List<Work> works;
        if (title != null && !title.isEmpty()) {
            // 根据标题查找
            works = workRepository.findByTitle(title);
        } else if (tag != null && !tag.isEmpty()) {
            // 根据标签查找
            Tag tagEntity = tagRepository.findByTagName(tag);
            if (tagEntity == null) {
                logger.debug("tag not found error");
                throw new TagNotFoundException(tag);
            }

            works = new ArrayList<>(tagEntity.getWorkSet());
        } else if (name != null && !name.isEmpty()) {
            // 根据作者查找
            User user = userRepository.findByName(name);
            if (user == null) {
                logger.debug("user not found error");
                throw new UserNotFoundException(name);
            }

            works = new ArrayList<>(user.getWorkSet());
        } else if (columnId != null) {
            // 根据专栏查找
            SpecialColumn column = columnRepository.findByColumnId(columnId);
            if (column == null) {
                logger.debug("column not found error");
                throw new ColumnNotFoundException(columnId);
            }

            works = new ArrayList<>(column.getWorkSet());
        } else {
            // 返回全部作品
            works = new ArrayList<>();
            workRepository.findAll().forEach(works::add);
        }

        // 按照发布最新的在前的顺序排序
        works.sort(Comparator.comparing(Work::getCreateTime).reversed());

        List<Map<String, Object>> workList = works.stream().map(work -> {
            Map<String, Object> map = new HashMap<>();
            map.put("work", WorkDto.wrap(work));
            map.put("tags", (new ArrayList<>(work.getTagSet())).stream().map(TagDto::wrap).collect(Collectors.toList()));
            map.put("author", work.getAuthor().getName());
            return map;
        }).collect(Collectors.toList());

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, workList, null, null);
    }

    public ResponseHolder showWorkDetail(Long userId, Long workId) {
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(userId);
        }

        Work work = workRepository.findByWorkId(workId);
        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(workId);
        }

        String authorName = work.getAuthor().getName();
        List<TagDto> tagList = (new ArrayList<>(work.getTagSet())).stream().map(TagDto::wrap).collect(Collectors.toList());

        if (!work.getPurchase().contains(user) && !work.getAuthor().equals(user)) {
            logger.debug("work not available error");
            throw new WorkNotAvailableException(userId, workId, authorName, tagList, WorkDto.wrap(work));
        }

        return new ResponseHolder(HttpStatus.OK.value(), authorName, null, tagList, WorkDetailDto.wrap(work), null);
    }
}
