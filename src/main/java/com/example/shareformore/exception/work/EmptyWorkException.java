package com.example.shareformore.exception.work;

public class EmptyWorkException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;
    private static final String Error = "The content of the work cannot be empty";

    public EmptyWorkException() {
        super(Error);
    }
}
