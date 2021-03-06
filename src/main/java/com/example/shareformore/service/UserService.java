package com.example.shareformore.service;

import com.example.shareformore.dto.UserDto;
import com.example.shareformore.entity.User;
import com.example.shareformore.exception.user.BadCredentialsException;
import com.example.shareformore.exception.user.BalanceOverflowException;
import com.example.shareformore.exception.user.UsernameHasBeenRegisteredException;
import com.example.shareformore.exception.user.UserNotFoundException;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.response.ResponseHolder;
import com.example.shareformore.security.jwt.JwtConfig;
import com.example.shareformore.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public ResponseHolder login(String name, String password){
        User loginUser = userRepository.findByName(name);
        if(loginUser == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException(name);
        }

        if(!loginUser.getPassword().equals(password)){
            //密码错误
            logger.debug("wrong password error");
            throw new BadCredentialsException("wrong password");
        }

        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setTimeLimit(600000); // 10min
        jwtConfig.setSecret("ShareForMore");
        JwtService jwtService = new JwtService(userRepository);
        JwtUtils jwtUtils = new JwtUtils(jwtConfig, jwtService);
        String token = jwtUtils.generateToken(name);
        logger.debug("Login success");

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, UserDto.wrap(loginUser), token);
    }

    public ResponseHolder register(String name, String password){
        if(userRepository.findByName(name)!= null){
            logger.debug("username used error");
            throw new UsernameHasBeenRegisteredException(name);
        }
        User registerUser = new User(name, password);
        userRepository.save(registerUser);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder pay(String username, int money){
        User user = userRepository.findByName(username);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException(username);
        }

        int newBalance = user.getBalance() + money;
        if(newBalance < 0){
            // 整数溢出
            logger.debug("balance overflow error");
            throw new BalanceOverflowException(user.getUsername(), money);
        }
        user.setBalance(newBalance);
        userRepository.save(user);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }

    public ResponseHolder info(String username, Long userId){
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException(username);
        }
        if(!username.equals(user.getUsername())){
            //不能暴露别人的余额
            user.setBalance(-1);
        }

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, UserDto.wrap(user), null);
    }

    public ResponseHolder changeIntro(String username, String selfIntro){
        User user = userRepository.findByName(username);
        if(user == null) {
            //用户不存在
            logger.debug("user not found error");
            throw new UserNotFoundException(username);
        }
        user.setSelf_introduction(selfIntro);
        userRepository.save(user);

        return new ResponseHolder(HttpStatus.OK.value(), "success", null, null, null, null);
    }
}
