package com.example.shareformore.exception.work;

import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.Work;

import java.util.List;

public class WorkNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = 5489192019064307564L;

    private final String authorName;
    private final List<Tag> tagList;
    private final Work work;

    public WorkNotAvailableException(long userId, long workId, String authorName, List<Tag> tagList, Work work) {
        super("Work " + workId + " is not available to user " + userId);
        this.authorName = authorName;
        this.tagList = tagList;
        this.work = work;
    }

    public String getAuthorName() {
        return authorName;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public Work getWork() {
        return work;
    }
}
