package com.example.shareformore.exception;

public class BalanceNotEnoughException extends RuntimeException {
    private static final long serialVersionUID = -5088763730874887906L;

    public BalanceNotEnoughException(String username, int price) {
        super("Balance of user " + username + " can not afford a price of " + price);
    }
}
