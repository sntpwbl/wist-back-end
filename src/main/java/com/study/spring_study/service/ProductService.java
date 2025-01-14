package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.mapper.ProductDTOMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductDTOMapper productDTOMapper;

    @Transactional
    public List<ProductDTO> findAll() {
        return repository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
    }
    
    @Transactional
    public ProductDTO findById(Long id) {
        return repository.findById(id)
            .map(productDTOMapper)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
    }
    
    @Transactional
    public Product createProduct(Product product, List<StoreLink> links){
        for (StoreLink link : links) {
            product.addLink(link); 
        }
        product.setBought(false);
        return repository.save(product);
    }
    
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPicture(productDTO.picture());

        List<StoreLink> updatedLinks = productDTO.links().stream().map(linkDTO -> {
            StoreLink link = new StoreLink();
            link.setStore(linkDTO.store());
            link.setUrl(linkDTO.url());
            link.setProduct(product); 
            return link;
        }).collect(Collectors.toList());

        product.getLinks().clear();
        product.getLinks().addAll(updatedLinks);

        return productDTOMapper.apply(repository.save(product));
    }
    @Transactional
    public ProductDTO changeProductBoughtStatus(Long id, boolean status) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setBought(status);

        return productDTOMapper.apply(repository.save(product));
    }
    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
