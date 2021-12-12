package com.example.shareformore.controller;

import com.example.shareformore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("name")String name, @RequestParam("password")String password) {
        logger.debug("get a login post");
        return userService.login(name, password).getResponseEntity();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("name")String name, @RequestParam("password")String password) {
        logger.debug("get a register post");
        return userService.register(name, password).getResponseEntity();
    }

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam("user_id")Long userId, @RequestParam("money")int money){
        logger.debug("get a pay post");
        return userService.pay(userId, money).getResponseEntity();
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestParam("user_id")Long userId){
        logger.debug("get a info post");
        return userService.info(userId).getResponseEntity();
    }

    @PostMapping("/changeIntro")
    public ResponseEntity<?> changeIntro(@RequestParam("user_id")Long userId, @RequestParam("self_introduction")String selfIntro) {
        logger.debug("get a changeIntro post");
        return userService.changeIntro(userId, selfIntro).getResponseEntity();
    }
}
