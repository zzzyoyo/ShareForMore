package com.example.shareformore.exception;

import com.example.shareformore.exception.column.ColumnNotFoundException;
import com.example.shareformore.exception.column.IllegalUpdateColumnException;
import com.example.shareformore.exception.tag.TagNameHasBeenUsedException;
import com.example.shareformore.exception.tag.TagNotFoundException;
import com.example.shareformore.exception.user.BadCredentialsException;
import com.example.shareformore.exception.user.BalanceOverflowException;
import com.example.shareformore.exception.user.UserNotFoundException;
import com.example.shareformore.exception.user.UsernameHasBeenRegisteredException;
import com.example.shareformore.exception.work.*;
import com.example.shareformore.response.ResponseHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * You can handle all of your exceptions here.
 */
@ControllerAdvice//这个注解指这个类是处理其他controller抛出的异常
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class, ColumnNotFoundException.class, TagNotFoundException.class, WorkNotFoundException.class})
    ResponseEntity<?> handleNotFoundException(RuntimeException ex) {
        return (new ResponseHolder(HttpStatus.NOT_FOUND.value(), "error", ex.getMessage(), null, null, null))
                .getResponseEntity();
    }

    @ExceptionHandler({BadCredentialsException.class, IllegalUpdateWorkException.class, IllegalUpdateColumnException.class})
    ResponseEntity<?> handleBadCredentialsException(RuntimeException ex) {
        return (new ResponseHolder(HttpStatus.FORBIDDEN.value(), "error", ex.getMessage(), null, null, null))
                .getResponseEntity();
    }

    @ExceptionHandler({WorkNotAvailableException.class})
    ResponseEntity<?> handleWorkNotAvailableException(WorkNotAvailableException ex) {
        return (new ResponseHolder(HttpStatus.FORBIDDEN.value(), "error", ex.getMessage(), ex.getTagList(), ex.getWork(), null))
                .getResponseEntity();
    }

    @ExceptionHandler({UsernameHasBeenRegisteredException.class, TagNameHasBeenUsedException.class, BalanceOverflowException.class,
            EmptyWorkException.class, IOException.class, AuthorBuyWorkException.class, WorkHasBeenBoughtException.class, InsufficientBalanceException.class, DeletePurchasedWorkException.class})
    ResponseEntity<?> handleBadRequestException(RuntimeException ex) {
        return (new ResponseHolder(HttpStatus.BAD_REQUEST.value(), "error", ex.getMessage(), null, null, null))
                .getResponseEntity();
    }
}
