package com.example.shareformore.dto;

import com.example.shareformore.entity.User;

import java.io.Serializable;


public class UserDto implements Serializable {
    private static final long serialVersionUID = 203974244986805454L;

    public final long user_id;
    public final String name;
    public final int balance;
    public final String self_introduction;

    private UserDto(User user) {
        this.user_id = user.getUserId();
        this.name = user.getName();
        this.balance = user.getBalance();
        this.self_introduction = user.getSelf_introduction();
    }

    public static UserDto wrap(User user) {
        return new UserDto(user);
    }
}
