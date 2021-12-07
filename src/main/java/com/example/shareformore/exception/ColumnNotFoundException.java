package com.example.shareformore.exception;

public class ColumnNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public ColumnNotFoundException(Long column_id) {
        super("Column with id " + column_id + " doesn't exist");
    }
}
