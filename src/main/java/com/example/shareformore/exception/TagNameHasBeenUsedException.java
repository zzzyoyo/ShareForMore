package com.example.shareformore.exception;

public class TagNameHasBeenUsedException extends RuntimeException  {
    private static final long serialVersionUID = -6074753940710869977L;

    public TagNameHasBeenUsedException(String tagName) {
        super("Tag name '" + tagName + "' has been used");
    }
}
