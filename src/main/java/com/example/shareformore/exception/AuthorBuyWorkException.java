package com.example.shareformore.exception;

public class AuthorBuyWorkException extends RuntimeException {
    private static final long serialVersionUID = 8166707722096024168L;

    public AuthorBuyWorkException() {
        super("Author cannot buy his own work.");
    }
}
