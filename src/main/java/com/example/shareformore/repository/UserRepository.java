package com.example.shareformore.repository;

import com.example.shareformore.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUserId(Long id);
    User findByName(String name);
}