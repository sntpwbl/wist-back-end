package com.study.spring_study.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService service;

    @GetMapping("/find-all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO productDTO = service.findById(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO dto) {
        List<StoreLink> links = dto.links().stream().map(linkRequest -> {
            StoreLink link = new StoreLink();
            link.setStore(linkRequest.store());
            link.setUrl(linkRequest.url());
            return link;
        }).collect(Collectors.toList());

        // Cria o produto
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPicture(dto.picture());

        Product savedProduct = service.createProduct(product, links);
        return ResponseEntity.ok(savedProduct);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
        ProductDTO updatedProduct = service.updateProduct(productDTO, id);
        return ResponseEntity.ok().body(updatedProduct);
    }

    @PatchMapping("/{id}/{status}")
    public ResponseEntity<ProductDTO> changeProductBoughtStatus(@PathVariable Long id, @PathVariable boolean status){
        ProductDTO updatedProductDTO = service.changeProductBoughtStatus(id, status);
        return ResponseEntity.ok().body(updatedProductDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
