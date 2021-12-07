package com.example.shareformore.exception;

public class WorkNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public WorkNotFoundException(Long work_id) {
        super("Work with id " + work_id + " doesn't exist");
    }
}