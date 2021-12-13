package com.example.shareformore.controller;

import com.example.shareformore.security.jwt.JwtUtils;
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
        logger.info("get a login post");
        return userService.login(name, password).getResponseEntity();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("name")String name, @RequestParam("password")String password) {
        logger.info("get a register post");
        return userService.register(name, password).getResponseEntity();
    }

    @GetMapping("/pay")
    public ResponseEntity<?> pay(@RequestHeader String token, @RequestParam("money")int money){
        logger.info("get a pay get");
        return userService.pay(JwtUtils.getUsername(token), money).getResponseEntity();
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestHeader String token, @RequestParam("user_id") Long userId){
        logger.info("get a info get");
        return userService.info(JwtUtils.getUsername(token), userId).getResponseEntity();
    }

    @PostMapping("/changeIntro")
    public ResponseEntity<?> changeIntro(@RequestHeader String token, @RequestParam("self_introduction")String selfIntro) {
        logger.info("get a changeIntro get");
        return userService.changeIntro(JwtUtils.getUsername(token), selfIntro).getResponseEntity();
    }
}
