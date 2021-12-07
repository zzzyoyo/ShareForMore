package com.example.shareformore.repository;

import com.example.shareformore.entity.Work;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends CrudRepository<Work, Long> {
    Work findByWorkId(Long id);
}
