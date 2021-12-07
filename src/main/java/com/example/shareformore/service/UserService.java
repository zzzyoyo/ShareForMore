package com.example.shareformore.service;

import com.example.shareformore.entity.User;
import com.example.shareformore.exception.BadCredentialsException;
import com.example.shareformore.exception.UsernameHasBeenRegisteredException;
import com.example.shareformore.exception.UsernameNotFoundException;
import com.example.shareformore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    //所有的exception都交给ControllerAdvisor来处理

    public Map<String,Object> login(String name, String password){
        Map<String, Object> map = new HashMap<>();
        User loginUser = userRepository.findByName(name);
        //用户不存在
        if(loginUser == null) {
            throw new UsernameNotFoundException(name);
        }
        //密码错误
        else if(!loginUser.getPassword().equals(password))
        {
            throw new BadCredentialsException("wrong password");
        }
        else{
            map.put("data", "success");
            map.put("obj", loginUser);
            logger.debug("Login success");
        }
        return map;
    }

    public Map<String, String> register(String name, String password){
        if(userRepository.findByName(name)!= null){
            throw new UsernameHasBeenRegisteredException(name);
        }
        User registerUser = new User(name, password);
        userRepository.save(registerUser);
        Map<String, String> map = new HashMap<>();
        map.put("data", "success");
        return map;
    }
}
