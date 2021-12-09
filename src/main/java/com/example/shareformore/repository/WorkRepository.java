package com.example.shareformore.repository;

import com.example.shareformore.entity.Work;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WorkRepository extends CrudRepository<Work, Long> {
    Work findByWorkId(Long id);
    List<Work> findByTitle(String title);
}
