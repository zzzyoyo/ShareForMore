package com.example.shareformore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User implements UserDetails {
    private static final long serialVersionUID = -8546972979375001850L;
    private static final String Initial_Introduction = "这个人很懒，什么简介都没写";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    @Column(unique = true)
    String name;
    String password;
    int balance;
    String self_introduction;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    Set<SpecialColumn> specialColumnSet;
    @JsonIgnore
    @OneToMany(mappedBy = "author")
    Set<Work> workSet;

    @JsonIgnore
    @JoinTable(name = "collection",joinColumns = @JoinColumn(name="userId"),inverseJoinColumns = @JoinColumn(name = "workId"))
    @ManyToMany
    Set<Work> collection;

    @JsonIgnore
    @JoinTable(name = "payment",joinColumns = @JoinColumn(name="userId"),inverseJoinColumns = @JoinColumn(name = "workId"))
    @ManyToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    Set<Work> payment;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Authority> authorities;

    public User() {
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.balance = 0;
        this.self_introduction = Initial_Introduction;
        this.specialColumnSet = new HashSet<>();
        this.workSet = new HashSet<>();
        this.payment = new HashSet<>();
        this.collection = new HashSet<>();
        this.authorities = new HashSet<>();
        this.specialColumnSet.add(new SpecialColumn(this));
    }

    public User(String name, String password, Authority authority) {
        this.name = name;
        this.password = password;
        this.balance = 0;
        this.self_introduction = Initial_Introduction;
        this.specialColumnSet = new HashSet<>();
        this.workSet = new HashSet<>();
        this.payment = new HashSet<>();
        this.collection = new HashSet<>();
        this.authorities = new HashSet<>();
        this.authorities.add(authority);
    }

    public void payWork(Work work) {
        this.balance = this.balance - work.getPrice();
        this.payment.add(work);
    }

    //重写equals方法, 最佳实践就是如下这种判断顺序:
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;
        return this.getUserId().equals(((User) obj).getUserId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getSelf_introduction() {
        return self_introduction;
    }

    public void setSelf_introduction(String self_introduction) {
        this.self_introduction = self_introduction;
    }

    @JsonIgnore
    public Set<SpecialColumn> getColumnSet() {
        return specialColumnSet;
    }

    public void setColumnSet(Set<SpecialColumn> specialColumnSet) {
        this.specialColumnSet = specialColumnSet;
    }

    public Set<Work> getWorkSet() {
        return workSet;
    }

    public void setWorkSet(Set<Work> workSet) {
        this.workSet = workSet;
    }

    public Set<Work> getPayment() {
        return payment;
    }

    public void setPayment(Set<Work> payment) {
        this.payment = payment;
    }

    public Set<Work> getCollection() {
        return collection;
    }

    public void setCollection(Set<Work> collection) {
        this.collection = collection;
    }
}
