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
        return ResponseEntity.ok(userService.login(name, password));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("name")String name, @RequestParam("password")String password) {
        logger.debug("get a register post");
        return ResponseEntity.ok(userService.register(name, password));
    }

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam("user_id")Long userId, @RequestParam("money")int money){
        logger.debug("get a pay post");
        return ResponseEntity.ok(userService.pay(userId, money));
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestParam("user_id")Long userId){
        logger.debug("get a info post");
        return ResponseEntity.ok(userService.info(userId));
    }

    @PostMapping("/changeIntro")
    public ResponseEntity<?> changeIntro(@RequestParam("user_id")Long userId, @RequestParam("self_introduction")String selfIntro) {
        logger.debug("get a changeIntro post");
        return ResponseEntity.ok(userService.changeIntro(userId, selfIntro));
    }
}
