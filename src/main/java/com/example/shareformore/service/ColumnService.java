package com.example.shareformore.service;

import com.example.shareformore.dto.ColumnDto;
import com.example.shareformore.entity.SpecialColumn;
import com.example.shareformore.entity.User;
import com.example.shareformore.entity.Work;
import com.example.shareformore.exception.*;
import com.example.shareformore.exception.column.ColumnNotFoundException;
import com.example.shareformore.exception.column.IllegalUpdateColumnException;
import com.example.shareformore.exception.user.UserNotFoundException;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.repository.WorkRepository;
import com.example.shareformore.response.ResponseHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    public ResponseHolder newColumn(Long authorId, String columnName, String description) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(authorId);
        }

        SpecialColumn column = new SpecialColumn(user, columnName, description);
        columnRepository.save(column);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder updateColumn(Long authorId, Long columnId, String columnName, String description) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(authorId);
        }

        SpecialColumn column = columnRepository.findByColumnId(columnId);
        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(columnId);
        }

        if (!column.getAuthor().equals(user)) {
            logger.debug("illegal update column error");
            throw new IllegalUpdateColumnException(user.getUsername(), columnId);
        }

        column.setColumnName(columnName);
        column.setDescription(description);
        column.setUpdateTime(new Timestamp(new Date().getTime()));
        columnRepository.save(column);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder deleteColumn(Long columnId) {
        SpecialColumn column = columnRepository.findByColumnId(columnId);
        if (column == null) {
            logger.debug("column not found error");
            throw new ColumnNotFoundException(columnId);
        }

        column.getWorkSet().clear();
        columnRepository.save(column);
        columnRepository.delete(column);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder listColumn(Long authorId) {
        User user = userRepository.findByUserId(authorId);
        if (user == null) {
            logger.debug("user not found error");
            throw new UserNotFoundException(authorId);
        }

        List<SpecialColumn> columns = new ArrayList<>(user.getColumnSet());
        List<ColumnDto> columnList = columns.stream().map(ColumnDto::wrap).collect(Collectors.toList());;

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, columnList, null, null);
    }
}
