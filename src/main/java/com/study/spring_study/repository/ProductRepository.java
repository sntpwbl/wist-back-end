package com.study.spring_study.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.study.spring_study.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    
    @Query("SELECT p FROM Product p WHERE p.user.id=:userId")
    public List<Product> findProductsByUserId(@Param("userId") Long userId);
}