package com.example.shareformore.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public UserNotFoundException(String userDescription) {
        super("User with " + userDescription + " doesn't exist");
    }
}
