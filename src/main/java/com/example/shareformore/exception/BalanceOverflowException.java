package com.example.shareformore.exception;

public class BalanceOverflowException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public BalanceOverflowException(String username, int money) {
        super("Balance of user " + username + " will exceed the max value after paying " + money);
    }
}
