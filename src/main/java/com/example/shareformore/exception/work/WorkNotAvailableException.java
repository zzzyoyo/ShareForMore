package com.example.shareformore.exception.work;

import com.example.shareformore.dto.TagDto;
import com.example.shareformore.dto.WorkDto;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.Work;

import java.util.List;

public class WorkNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = 5489192019064307564L;

    private final List<TagDto> tagList;
    private final WorkDto work;

    public WorkNotAvailableException(long userId, long workId, List<TagDto> tagList, WorkDto work) {
        super("Work with id: " + workId + " is not available to user with id: " + userId);
        this.tagList = tagList;
        this.work = work;
    }

    public WorkNotAvailableException(String username, long workId) {
        super("Work with id: " + workId + " is not available to user with name: " + username);
        this.tagList = null;
        this.work = null;
    }


    public List<TagDto> getTagList() {
        return tagList;
    }

    public WorkDto getWork() {
        return work;
    }
}
