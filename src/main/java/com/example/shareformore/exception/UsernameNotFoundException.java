package com.example.shareformore.exception;

public class UsernameNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public  UsernameNotFoundException(String username) {
        super("User with name '" + username + "' doesn't exist");
    }
}
