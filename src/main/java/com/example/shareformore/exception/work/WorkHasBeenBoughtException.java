package com.example.shareformore.exception.work;

public class WorkHasBeenBoughtException extends RuntimeException {
    private static final long serialVersionUID = 4078339436604196922L;

    public WorkHasBeenBoughtException() {
        super("This work has been bought by the user.");
    }
}
