package com.example.shareformore.service;

import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;
import com.example.shareformore.exception.*;
import com.example.shareformore.exception.user.UserNotFoundException;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

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

    public Map uploadWork(String author, Long column_id, List<Long> tag_list, String title, String description, String content, int price, MultipartFile image) throws IOException {
        User user = userRepository.findByName(author);

        if ((content == null || content.isBlank()) && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        SpecialColumn column = null;
        if (column_id != null) {
            column = columnRepository.findByColumnId(column_id);
            if (column == null && column_id >= 0) {
                logger.debug("column not found error");
                throw new ColumnNotFoundException(column_id);
            }
        }

        Set<Tag> tagSet = null;
        if (tag_list != null) {
            tagSet = getTagSet(tag_list);
        }

        Work work = new Work(user, column, title, description, content, image.getBytes(), price, tagSet);
        workRepository.save(work);

        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map updateWork(String author, Long work_id, Long column_id, List<Long> tag_list, String title, String description, String content, int price, MultipartFile image) throws IOException {
        if ((content == null || content.isBlank()) && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        User user = userRepository.findByName(author);
        Work work = workRepository.findByWorkId(work_id);

        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(work_id);
        }

        SpecialColumn column = null;
        if (column_id != null) {
            column = columnRepository.findByColumnId(column_id);
            if (column == null && column_id >= 0) {
                logger.debug("column not found error");
                throw new ColumnNotFoundException(column_id);
            }
        }

        if (!user.equals(work.getAuthor())) {
            logger.debug("tag not found error");
            throw new IllegalUpdateException(author, work_id);
        }


        Set<Tag> tagSet = null;
        if (tag_list != null) {
            tagSet = getTagSet(tag_list);
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

        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map payWork(String username, Long work_id) {
        User user = userRepository.findByName(username);
        Work work = workRepository.findByWorkId(work_id);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(username);
        }
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
        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
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

    public Map<String, Object> listWork(String title, String tag, String name, Long columnId) {
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
            Iterable<Work> works1 = workRepository.findAll();
            workRepository.findAll().forEach(works::add);
        }

        // 按照发布最新的在前的顺序排序
        works.sort(Comparator.comparing(Work::getCreateTime).reversed());

        Map<String, Object> map = new HashMap<>();
        map.put("list", works);
        return map;
    }

    public Map<String, Object> showWorkDetail(Long userId, Long workId) {
        User user = userRepository.findByUserId(userId);
        Work work = workRepository.findByWorkId(workId);
        if(user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(userId);
        }
        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(workId);
        }

        boolean isAvailable = work.getPurchase().contains(user) || work.getAuthor().equals(user);


        Map<String, Object> map = new HashMap<>();
        map.put("isAvailable", isAvailable); // 有无权限
        map.put("data", (isAvailable) ? work.getAuthor().getName() : "forbidden");
        map.put("obj", work);
        return map;
    }

    public Map<String, Object> showWorkTags(Long workId) {
        Work work = workRepository.findByWorkId(workId);
        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(workId);
        }

        List<Tag> tags = new ArrayList<>(work.getTagSet());

        Map<String, Object> map = new HashMap<>();
        map.put("list", tags);
        return map;
    }
}
