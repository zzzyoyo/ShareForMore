package com.example.shareformore.repository;

import com.example.shareformore.entity.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<Authority, Long> {
    Authority findByAuthId(Long id);
}
