package com.example.shareformore.service;

import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;
import com.example.shareformore.exception.*;
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
    private UserRepository userRepository;
    private WorkRepository workRepository;
    private ColumnRepository columnRepository;
    private TagRepository tagRepository;

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
        SpecialColumn column = columnRepository.findByColumnId(column_id);

        if (content.isBlank() && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(column_id);
        }

        Set<Tag> tagSet = getTagSet(tag_list);
        Work work = new Work(user, column, title, description, content, image.getBytes(), price, tagSet);
        workRepository.save(work);

        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map updateWork(String author, Long work_id, Long column_id, List<Long> tag_list, String title, String description, String content, int price, MultipartFile image) throws IOException {
        if (content.isBlank() && image == null) {
            logger.debug("empty work error");
            throw new EmptyWorkException();
        }

        User user = userRepository.findByName(author);
        SpecialColumn column = columnRepository.findByColumnId(column_id);
        Work work = workRepository.findByWorkId(work_id);

        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(work_id);
        }

        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(column_id);
        }

        if (!user.equals(work.getAuthor())) {
            logger.debug("tag not found error");
            throw new IllegalUpdateException(author, work_id);
        }

        Set<Tag> tagSet = getTagSet(tag_list);
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
        return null;
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
}
