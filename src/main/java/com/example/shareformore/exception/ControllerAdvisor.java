package com.example.shareformore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * You can handle all of your exceptions here.
 */
@ControllerAdvice//这个注解指这个类是处理其他controller抛出的异常
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    //当controller中抛出用户不存在异常时会转到这个方法中处理
    @ExceptionHandler({UserNotFoundException.class, ColumnNotFoundException.class, TagNotFoundException.class, WorkNotFoundException.class})
    ResponseEntity<?> handleUserNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class, IllegalUpdateException.class})
    ResponseEntity<?> handleBadCredentialsException(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({UsernameHasBeenRegisteredException.class, BalanceOverflowException.class, EmptyWorkException.class, IOException.class})
    ResponseEntity<?> handleBadRequestException(RuntimeException ex) {
        return new ResponseEntity<>(getBody(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 出错时按照前端的要求返回的body
     * @param message
     * @return body
     */
    private Map<String, String> getBody(String message){
        Map<String, String> body = new HashMap<>();
        body.put("data", "error");
        body.put("message", message);
        return body;
    }
}
