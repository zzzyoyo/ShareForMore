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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ColumnService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final ColumnRepository columnRepository;

    Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    public ColumnService(UserRepository userRepository, WorkRepository workRepository,  ColumnRepository columnRepository) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.columnRepository = columnRepository;
    }

    public Map<String, Object> newColumn(Long authorId, String columnName, String description) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + authorId + "'");
        }

        SpecialColumn column = columnRepository.findByColumnName(columnName);
        if(column != null){
            // 有个问题：一个用户注册的专栏名是否其他用户没法再用？
            logger.debug("column name used error");

            Map<String, Object> map = new HashMap<>();
            map.put("data", "error");
            return map;
        }

        column = new SpecialColumn(user, columnName, description);
        columnRepository.save(column);

        Map<String, Object> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, Object> updateColumn(Long authorId, Long columnId, String columnName, String description) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + authorId + "'");
        }

        SpecialColumn column = columnRepository.findByColumnId(columnId);
        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(columnId);
        }

        if (!column.getAuthor().equals(user)) {
            logger.debug("illegal update error");
            throw new IllegalUpdateException(user.getUsername(), columnId);
        }

        column.setColumnName(columnName);
        column.setDescription(description);
        columnRepository.save(column);

        Map<String, Object> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, Object> addColumn(Long columnId, Long workId) {
        SpecialColumn column = columnRepository.findByColumnId(columnId);
        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(columnId);
        }

        Work work = workRepository.findByWorkId(workId);
        if (work == null) {
            logger.debug("work not found error");
            throw new WorkNotFoundException(workId);
        }

        column.getWorkSet().add(work);
        columnRepository.save(column);

        Map<String, Object> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, Object> deleteColumn(Long columnId) {
        SpecialColumn column = columnRepository.findByColumnId(columnId);
        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(columnId);
        }

        column.getWorkSet().clear();
        columnRepository.save(column);
        columnRepository.delete(column);

        Map<String, Object> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, Object> listColumn(Long authorId) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + authorId + "'");
        }

        List<SpecialColumn> columns = new ArrayList<>(user.getColumnSet());

        Map<String, Object> map = new HashMap<>();
        map.put("list", columns);
        return map;
    }
}
