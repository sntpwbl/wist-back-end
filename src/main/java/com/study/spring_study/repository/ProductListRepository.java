package com.study.spring_study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.spring_study.model.ProductList;

@Repository
public interface ProductListRepository extends JpaRepository<ProductList, Long>{
    
}
