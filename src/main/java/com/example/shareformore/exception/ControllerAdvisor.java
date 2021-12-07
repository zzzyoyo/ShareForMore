package com.example.shareformore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * You can handle all of your exceptions here.
 */
@ControllerAdvice//这个注解指这个类是处理其他controller抛出的异常
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    //当controller中抛出用户名不存在异常时会转到这个方法中处理
    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        logger.debug("user not found error");
        Map<String, String> body = new HashMap<>();
        body.put("data", "error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 用户名密码不匹配
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        logger.debug("wrong password error");
        Map<String, String> body = new HashMap<>();
        body.put("data", "error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    //用户名已被注册
    @ExceptionHandler(UsernameHasBeenRegisteredException.class)
    ResponseEntity<?> handlerUsernameHasBeenRegisteredException(UsernameHasBeenRegisteredException ex) {
        logger.debug("username used error");
        Map<String, String> body = new HashMap<>();
        body.put("data", "error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
