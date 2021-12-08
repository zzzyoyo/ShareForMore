package com.example.shareformore.exception;

public class IllegalUpdateException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public IllegalUpdateException(String username, Long work_id) {
        super("User: " + username + " has no permission to update the work: " + work_id);
    }
}