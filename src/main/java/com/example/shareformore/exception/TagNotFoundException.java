package com.example.shareformore.exception;

public class TagNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public TagNotFoundException(Long tag_id) {
        super("Tag with id " + tag_id + " doesn't exist");
    }
}
