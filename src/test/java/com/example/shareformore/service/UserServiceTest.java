package com.example.shareformore.service;


import com.example.shareformore.ShareForMoreApplication;
import com.example.shareformore.entity.User;
import com.example.shareformore.exception.user.UsernameHasBeenRegisteredException;
import com.example.shareformore.repository.AuthRepository;
import com.example.shareformore.repository.ColumnRepository;
import com.example.shareformore.repository.TagRepository;
import com.example.shareformore.repository.UserRepository;
import com.example.shareformore.response.ResponseHolder;

import com.example.shareformore.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest{
    @Autowired
    private UserRepository userRepository;

    /*
    测试前需要先注册user
    name:zhangsan
    pwd:zhangsan111
     */

    @Test
    void login() {
        UserService userService=new UserService(userRepository);
        assertEquals("200 OK",userService.login("zhangsan","zhangsan111").getResponseEntity()
                .getStatusCode().toString());

        //用于测试异常的出现
        try {
            userService.login("zhangnimasan","zhangsan111");
            fail();
        } catch (Exception e) {
            assertEquals( "User with name: zhangnimasan doesn't exist",e.getMessage());
        }

        try {
            userService.login("zhangsan","zhangsan222");
            fail();
        } catch (Exception e) {
            assertEquals("com.example.shareformore.exception.user.BadCredentialsException",e.getClass().getName());
        }

    }

    @Test
    void register() {
        UserService userService=new UserService(userRepository);
        userService.register("zhangsaanx","zhangsan111");
        assertEquals("zhangsaanx",userRepository.findByName("zhangsaanx").getName());
        assertEquals("zhangsan111",userRepository.findByName("zhangsaanx").getPassword());

        //用于测试异常的出现
        try {
            userService.register("zhangsan","zhangsan111");
            fail();
        } catch (Exception e) {
            assertEquals( "Username 'zhangsan' has been registered",e.getMessage());
        }
    }

    @Test
    void pay() {
        UserService userService=new UserService(userRepository);
        User user=userRepository.findByName("zhangsan");
        user.setBalance(10);
        userRepository.save(user);

        //用于测试异常的出现
        try {
            userService.pay("lisi",111);
            fail();
        } catch (Exception e) {
            assertEquals( "User with name: lisi doesn't exist",e.getMessage());
        }

        //用于测试异常的出现

        try {
            userService.pay("zhangsan", -99999);
            fail();
        } catch (Exception e) {
            assertEquals("com.example.shareformore.exception.user.BalanceOverflowException",e.getClass().getName());

        }

        userService.pay("zhangsan",-1);
        assertEquals(9,userRepository.findByName("zhangsan").getBalance());



    }

    @Test
    void info() {
        UserService userService=new UserService(userRepository);
        //用于测试异常的出现
        try {
            userService.info("zhangsan",0L);
            fail();
        } catch (Exception e) {
            assertEquals( "User with name: zhangsan doesn't exist",e.getMessage());
        }
        //System.out.println(userService.info(userRepository.findByName("zhangsan").getUserId()).getResponseEntity().getBody().toString());
        assertEquals("200 OK",userService.info("zhangsan",userRepository.findByName("zhangsan").getUserId()).getResponseEntity()
                .getStatusCode().toString());
        assertTrue(userService.info("zhangsan",userRepository.findByName("zhangsan").getUserId()).getResponseEntity().getBody().toString().contains("UserDto"));
        assertTrue(userService.info("zhangsaanx",userRepository.findByName("zhangsan").getUserId()).getResponseEntity().getBody().toString().contains("UserDto"));




    }

    @Test
    void changeIntro() {
        UserService userService=new UserService(userRepository);
        //用于测试异常的出现
        try {
            userService.changeIntro("lisi","Hello");
            fail();
        } catch (Exception e) {
            assertEquals( "User with name: lisi doesn't exist",e.getMessage());
        }
        User user=userRepository.findByName("zhangsan");
        user.setSelf_introduction("zhangsan!!!");
        userRepository.save(user);
        String before=user.getSelf_introduction();
        userService.changeIntro(user.getUsername(),"Hello yo!!!");

        user=userRepository.findByName("zhangsan");
        assertNotEquals(before,user.getSelf_introduction());
        assertEquals("Hello yo!!!",user.getSelf_introduction());


    }
}