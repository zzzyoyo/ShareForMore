package com.example.shareformore.service;

import com.example.shareformore.entity.User;
import com.example.shareformore.exception.BadCredentialsException;
import com.example.shareformore.exception.BalanceOverflowException;
import com.example.shareformore.exception.UsernameHasBeenRegisteredException;
import com.example.shareformore.exception.UserNotFoundException;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.security.jwt.JwtConfig;
import com.example.shareformore.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 所有的exception都交给ControllerAdvisor来处理
     */

    public Map<String,Object> login(String name, String password){
        Map<String, Object> map = new HashMap<>();
        User loginUser = userRepository.findByName(name);
        if(loginUser == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException("name '" + name + "'");
        }
        else if(!loginUser.getPassword().equals(password)){
            //密码错误
            logger.debug("wrong password error");
            throw new BadCredentialsException("wrong password");
        }
        else{
            JwtConfig jwtConfig = new JwtConfig();
            jwtConfig.setTimeLimit(600000); // 10min
            jwtConfig.setSecret("ShareForMore");
            JwtService jwtService = new JwtService(userRepository);
            JwtUtils jwtUtils = new JwtUtils(jwtConfig, jwtService);
            String token = jwtUtils.generateToken(name);
            map.put("token",token);
            map.put("data", "success");
            map.put("obj", loginUser);
            logger.debug("Login success");
        }
        return map;
    }

    public Map<String, String> register(String name, String password){
        if(userRepository.findByName(name)!= null){
            logger.debug("username used error");
            throw new UsernameHasBeenRegisteredException(name);
        }
        User registerUser = new User(name, password);
        userRepository.save(registerUser);
        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, String> pay(Long userId, int money){
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + userId + "'");
        }
        int newBalance = user.getBalance() + money;
        if(newBalance < 0){
            // 整数溢出
            logger.debug("balance overflow error");
            throw new BalanceOverflowException(user.getUsername(), money);
        }
        user.setBalance(newBalance);
        userRepository.save(user);
        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }

    public Map<String, Object> info(Long userId){
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + userId + "'");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("obj", user);
        return map;
    }

    public Map<String, Object> changeIntro(Long userId, String selfIntro){
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException("id '" + userId + "'");
        }
        user.setSelf_introduction(selfIntro);
        userRepository.save(user);
        Map<String, Object> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }
}
