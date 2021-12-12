package com.example.shareformore.exception.column;

public class IllegalUpdateColumnException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public IllegalUpdateColumnException(String username, Long columnId) {
        super("User: " + username + " has no permission to update the column with id: " + columnId);
    }
}