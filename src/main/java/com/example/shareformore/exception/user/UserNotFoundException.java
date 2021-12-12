package com.example.shareformore.exception.user;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public UserNotFoundException(String name) {
        super("User with name: " + name + " doesn't exist");
    }

    public UserNotFoundException(Long id) {
        super("User with id: " + id + " doesn't exist");
    }
}
