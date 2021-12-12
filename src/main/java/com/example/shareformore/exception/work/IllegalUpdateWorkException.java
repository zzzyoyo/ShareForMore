package com.example.shareformore.exception.work;

public class IllegalUpdateWorkException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public IllegalUpdateWorkException(String username, Long workId) {
        super("User: " + username + " has no permission to update the work with id: " + workId);
    }
}