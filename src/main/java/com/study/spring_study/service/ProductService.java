package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional
    public List<ProductDTO> findAll() {
        return repository.findAll().stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPicture(product.getPicture());
            dto.setLinks(product.getLinks().stream().map(link -> {
                StoreLinkDTO linkDTO = new StoreLinkDTO();
                linkDTO.setStore(link.getStore());
                linkDTO.setUrl(link.getUrl());
                return linkDTO;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPicture(product.getPicture());
        productDTO.setLinks(product.getLinks().stream().map(link -> {
            StoreLinkDTO linkDTO = new StoreLinkDTO();
            linkDTO.setStore(link.getStore());
            linkDTO.setUrl(link.getUrl());
            return linkDTO;
        }).collect(Collectors.toList()));

        return productDTO;
    }
    
    @Transactional
    public Product createProduct(Product product, List<StoreLink> links){
        for (StoreLink link : links) {
            product.addLink(link); 
        }
        return repository.save(product);
    }
    
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPicture(productDTO.getPicture());

        List<StoreLink> updatedLinks = productDTO.getLinks().stream().map(linkDTO -> {
            StoreLink link = new StoreLink();
            link.setStore(linkDTO.getStore());
            link.setUrl(linkDTO.getUrl());
            link.setProduct(product); 
            return link;
        }).collect(Collectors.toList());

        product.getLinks().clear();
        product.getLinks().addAll(updatedLinks);

        Product updatedProduct = repository.save(product);

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(updatedProduct.getId());
        updatedProductDTO.setName(updatedProduct.getName());
        updatedProductDTO.setDescription(updatedProduct.getDescription());
        updatedProductDTO.setPicture(updatedProduct.getPicture());
        updatedProductDTO.setLinks(updatedProduct.getLinks().stream().map(link -> {
            StoreLinkDTO linkDTO = new StoreLinkDTO();
            linkDTO.setStore(link.getStore());
            linkDTO.setUrl(link.getUrl());
            return linkDTO;
        }).collect(Collectors.toList()));

        return updatedProductDTO;
    }
    @Transactional
public ProductDTO changeProductBoughtStatus(Long id, boolean status) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("No product found for this ID."));

    product.setBought(status);
    
    product = repository.save(product);

    ProductDTO productDTO = new ProductDTO();
    productDTO.setId(product.getId());
    productDTO.setName(product.getName());
    productDTO.setDescription(product.getDescription());
    productDTO.setPicture(product.getPicture());
    productDTO.setBought(product.isBought()); 

    productDTO.setLinks(product.getLinks().stream().map(link -> {
        StoreLinkDTO linkDTO = new StoreLinkDTO();
        linkDTO.setStore(link.getStore());
        linkDTO.setUrl(link.getUrl());
        return linkDTO;
    }).collect(Collectors.toList()));

    return productDTO;
}
    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
