package com.study.spring_study.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.spring_study.model.ProductList;

@Repository
public interface ListRepository extends JpaRepository<ProductList, Long> {
    public List<ProductList> findByUserId(@Param("userId") Long userId);
}
