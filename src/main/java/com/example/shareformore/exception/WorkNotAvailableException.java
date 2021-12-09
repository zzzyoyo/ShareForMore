package com.example.shareformore.exception;

public class WorkNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = 5489192019064307564L;

    public WorkNotAvailableException(long userId, long workId) {
        super("Work " + workId + " is not available to user " + userId);
    }
}
