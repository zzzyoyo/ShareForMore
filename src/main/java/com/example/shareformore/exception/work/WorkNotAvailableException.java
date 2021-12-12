package com.example.shareformore.exception.work;

import com.example.shareformore.dto.TagDto;
import com.example.shareformore.dto.WorkDto;
import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.Work;

import java.util.List;

public class WorkNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = 5489192019064307564L;

    private final String authorName;
    private final List<TagDto> tagList;
    private final WorkDto work;

    public WorkNotAvailableException(long userId, long workId, String authorName, List<TagDto> tagList, WorkDto work) {
        super("Work " + workId + " is not available to user " + userId);
        this.authorName = authorName;
        this.tagList = tagList;
        this.work = work;
    }

    public String getAuthorName() {
        return authorName;
    }

    public List<TagDto> getTagList() {
        return tagList;
    }

    public WorkDto getWork() {
        return work;
    }
}
