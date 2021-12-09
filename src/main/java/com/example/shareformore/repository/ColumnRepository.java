package com.example.shareformore.repository;

import com.example.shareformore.entity.SpecialColumn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends CrudRepository<SpecialColumn, Long> {
    SpecialColumn findByColumnId(Long id);
    SpecialColumn findByColumnName(String columnName);
}