package com.example.shareformore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Authority implements GrantedAuthority {
    private static final long serialVersionUID = -8546972979375001850L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long authId;

    @Column(unique = true)
    private String authority;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<User> Users = new HashSet<>();

    public Authority() { }

    public Authority(String authority){
        this.authority = authority;
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Authority))
            return false;
        if (obj == this)
            return true;
        return this.getId().equals(((Authority) obj).getId());
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return Users;
    }

    public void setUsers(Set<User> Users) {
        this.Users = Users;
    }

    public Long getId() {
        return authId;
    }

    public void setId(Long id) {
        this.authId = id;
    }
}
