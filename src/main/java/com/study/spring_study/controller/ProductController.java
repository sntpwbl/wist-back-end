package com.study.spring_study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.model.Product;
import com.study.spring_study.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService service;

    @GetMapping("/find-all")
    public ResponseEntity<List<Product>> findAllProducts() {
        return ResponseEntity.ok().body(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product));
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id){
        return ResponseEntity.ok().body(service.updateProduct(product, id));
    }

    @PatchMapping("/bought/{id}")
    public ResponseEntity<Product> changeProductBoughtStatus(@PathVariable Long id, @RequestParam boolean bought){
        Product boughtProduct = service.changeBoughtStatus(id, bought);
        return ResponseEntity.ok().body(boughtProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
