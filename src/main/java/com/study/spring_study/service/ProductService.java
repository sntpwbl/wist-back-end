package com.study.spring_study.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.model.Product;
import com.study.spring_study.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public List<Product> findAll(){
        return repository.findAll();
    }

    public Product findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("No product found for this ID."));
    }
    
    public Product createProduct(Product product){
        return repository.save(product);
    }
    
    public Product updateProduct(Product product, Long id){
        repository.findById(id).orElseThrow(() -> new NotFoundException("No product found for this ID."));
        
        return repository.save(product);
    }
    
    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
