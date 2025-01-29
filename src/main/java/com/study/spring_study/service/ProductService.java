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
import com.study.spring_study.exception.UnmatchedTokenAndReqIdsException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.model.User;
import com.study.spring_study.repository.ProductRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    public List<EntityModel<ProductDTO>> findAll() {
        
        List<EntityModel<ProductDTO>> products = productRepository.findAll().stream().map(product ->{
                ProductDTO dto = mapper.productToDTO(product);
                EntityModel<ProductDTO> model = EntityModel.of(dto);
                model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());

                return model;
            }
        ).collect(Collectors.toList());

        return products;
    }
    public List<EntityModel<ProductDTO>> findProductsByUserId(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(request);
        List<EntityModel<ProductDTO>> products = productRepository.findByUserId(userId).stream().map(product ->{
            ProductDTO dto = mapper.productToDTO(product);
            EntityModel<ProductDTO> model = EntityModel.of(dto);
            model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
            model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
            model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
            
            return model;
        }
        ).collect(Collectors.toList());
        System.out.println(products.size());

        return products;
    }
    
    public EntityModel<ProductDTO> findById(Long id) {
        ProductDTO dto = productRepository.findById(id)
            .map(p -> mapper.productToDTO(p))
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        EntityModel<ProductDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, id)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(id)).withSelfRel());

        return model;
    }
    
    public EntityModel<ProductDTO> createProduct(Product product, List<StoreLink> links, HttpServletRequest request) throws NullRequiredObjectException{
        if(product == null) throw new NullRequiredObjectException();
        User creatorUser = userRepository.findById(jwtUtil.getUserIdFromToken(request)).orElseThrow(() -> new NotFoundException("No user found for this ID."));
        for (StoreLink link : links) {
            product.addLink(link); 
        }
        product.setBought(false);
        product.setUser(creatorUser);
        ProductDTO dto = mapper.productToDTO(productRepository.save(product));
        
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        
        return model;
    }
    
    public EntityModel<ProductDTO> updateProduct(ProductDTO productDTO, Long id) {
        if(productDTO == null) throw new NullRequiredObjectException();
        Product product = productRepository.findById(id)
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
        productRepository.save(product);
        ProductDTO dto = mapper.productToDTO(product);
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        return model;
    }

    public EntityModel<ProductDTO> changeProductBoughtStatus(Long id, boolean status) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

        product.setBought(status);
        productRepository.saveAndFlush(product);

        ProductDTO dto = mapper.productToDTO(product);
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id())).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id())).withSelfRel());
        return model;
    }
    
    public void deleteProduct(Long id){
        productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        productRepository.deleteById(id);
    }

    public void tokenUserIdEqualToReqUserIdVerification(Long tokenUserId, Long reqUserId){
        if(tokenUserId!=reqUserId) throw new UnmatchedTokenAndReqIdsException();
    }
}
