package com.example.shareformore.repository;

import com.example.shareformore.entity.Tag;
import com.example.shareformore.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    Tag findByTagId(Long id);
    Tag findByTagName(String name);
}
