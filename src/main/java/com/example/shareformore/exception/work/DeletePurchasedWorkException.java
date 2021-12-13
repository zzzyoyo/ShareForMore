package com.example.shareformore.exception.work;

public class DeletePurchasedWorkException extends RuntimeException {
    private static final long serialVersionUID = -6074753940710869977L;

    public DeletePurchasedWorkException(Long workId){super("You can't delete the work with id: " + workId + " which has been purchased by other users");}
}
