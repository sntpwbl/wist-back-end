package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.study.spring_study.controller.ProductController;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.mapper.ProductDTOMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.repository.ProductRepository;


@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    private final ProductDTOMapper productDTOMapper;

    public ProductService(ProductDTOMapper productDTOMapper) {
        this.productDTOMapper = productDTOMapper;
    }
    public List<ProductDTO> findAll() {
        return repository.findAll().stream().map(productDTOMapper).collect(Collectors.toList());
    }
    
    public EntityModel<ProductDTO> findById(Long id) {

        ProductDTO product = repository.findById(id)
            .map(productDTOMapper)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        
        EntityModel<ProductDTO> model = EntityModel.of(product);
        model.add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
        return model;
    }
    
    public Product createProduct(Product product, List<StoreLink> links){
        for (StoreLink link : links) {
            product.addLink(link); 
        }
        product.setBought(false);
        return repository.save(product);
    }
    
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setName(productDTO.name());
        product.setDescription(productDTO.description());
        product.setPicture(productDTO.picture());

        List<StoreLink> updatedLinks = productDTO.storeLinks().stream().map(linkDTO -> {
            StoreLink link = new StoreLink();
            link.setStore(linkDTO.store());
            link.setUrl(linkDTO.url());
            link.setProduct(product); 
            return link;
        }).collect(Collectors.toList());

        product.getStoreLinks().clear();
        product.getStoreLinks().addAll(updatedLinks);

        return productDTOMapper.apply(repository.save(product));
    }

    public ProductDTO changeProductBoughtStatus(Long id, boolean status) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setBought(status);

        return productDTOMapper.apply(repository.saveAndFlush(product));
    }
    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
