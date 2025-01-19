package com.study.spring_study.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.study.spring_study.controller.ProductController;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.mapper.ProductDTOMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.repository.ProductRepository;

//TO-DO: add hateoas to service methods
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    private final ProductDTOMapper productDTOMapper;

    public ProductService(ProductDTOMapper productDTOMapper) {
        this.productDTOMapper = productDTOMapper;
    }

    public List<EntityModel<ProductDTO>> findAll() {
        
        List<EntityModel<ProductDTO>> products = repository.findAll().stream().map(product ->{
                ProductDTO dto = productDTOMapper.apply(product);
                EntityModel<ProductDTO> model = EntityModel.of(dto);
                model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());

                return model;
            }
        ).collect(Collectors.toList());

        return products;
    }
    
    public EntityModel<ProductDTO> findById(Long id) {
        ProductDTO dto = repository.findById(id)
            .map(productDTOMapper)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        EntityModel<ProductDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, id)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(id)).withSelfRel());

        return model;
    }
    
    public EntityModel<ProductDTO> createProduct(Product product, List<StoreLink> links) throws NullRequiredObjectException{
        if(product == null) throw new NullRequiredObjectException();
        for (StoreLink link : links) {
            product.addLink(link); 
        }
        product.setBought(false);
        ProductDTO dto = productDTOMapper.apply(repository.save(product));
        
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        
        return model;
    }
    
    public EntityModel<ProductDTO> updateProduct(ProductDTO productDTO, Long id) {
        if(productDTO == null) throw new NullRequiredObjectException();
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
        repository.save(product);
        ProductDTO dto = productDTOMapper.apply(product);
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        return model;
    }

    public EntityModel<ProductDTO> changeProductBoughtStatus(Long id, boolean status) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setBought(status);
        repository.saveAndFlush(product);

        ProductDTO dto = productDTOMapper.apply(product);
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        return model;
    }
    
    public void deleteProduct(Long id){
        repository.deleteById(id);
    }
}
