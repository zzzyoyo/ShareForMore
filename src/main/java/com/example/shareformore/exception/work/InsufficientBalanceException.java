package com.example.shareformore.exception.work;

public class InsufficientBalanceException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public  InsufficientBalanceException(int price) {
        super("Your balance is less than $" + price + ", not enough to complete the payment\n");
    }
}
