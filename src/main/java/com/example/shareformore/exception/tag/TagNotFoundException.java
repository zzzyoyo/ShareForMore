package com.example.shareformore.exception.tag;

public class TagNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public TagNotFoundException(Long tag_id) {
        super("Tag with id " + tag_id + " doesn't exist");
    }

    public TagNotFoundException(String tag_name) {
        super("Tag with name " + tag_name + " doesn't exist");
    }
}
