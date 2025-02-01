package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.model.User;
import com.study.spring_study.repository.ProductRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtUtil;
import com.study.spring_study.utils.Utils;

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

    @Autowired
    private Utils utils;

    public List<EntityModel<ProductDTO>> findAll() {
        
        List<EntityModel<ProductDTO>> products = productRepository.findAll().stream().map(product ->{
                ProductDTO dto = mapper.productToDTO(product);
                EntityModel<ProductDTO> model = EntityModel.of(dto);

                return model;
            }
        ).collect(Collectors.toList());

        return products;
    }
    public List<EntityModel<ProductDTO>> findProductsByUserId(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(request);
        List<EntityModel<ProductDTO>> products = productRepository.findByUserId(userId).stream().map(product ->{
            ProductDTO dto = mapper.productToDTO(product);
            return utils.createProductHateoasModel(dto, request);
        }
        ).collect(Collectors.toList());

        return products;
    }
    
    public EntityModel<ProductDTO> findById(Long id, HttpServletRequest request) {
        ProductDTO dto = productRepository.findById(id)
            .map(p -> mapper.productToDTO(p))
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

            return utils.createProductHateoasModel(dto, request);
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
        
        return utils.createProductHateoasModel(dto, request);
    }
    
    public EntityModel<ProductDTO> updateProduct(ProductDTO productDTO, Long id, HttpServletRequest request) {
        if(productDTO == null) throw new NullRequiredObjectException();
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
            utils.tokenUserIdEqualToReqUserIdVerification(product.getUser().getId(), jwtUtil.getUserIdFromToken(request));

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
        return utils.createProductHateoasModel(dto, request);

    }

    public EntityModel<ProductDTO> changeProductBoughtStatus(Long id, boolean status, HttpServletRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));

            utils.tokenUserIdEqualToReqUserIdVerification(product.getUser().getId(), jwtUtil.getUserIdFromToken(request));

        product.setBought(status);
        productRepository.saveAndFlush(product);

        ProductDTO dto = mapper.productToDTO(product);
        return utils.createProductHateoasModel(dto, request);

    }
    
    public void deleteProduct(Long id, HttpServletRequest request){
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        
            utils.tokenUserIdEqualToReqUserIdVerification(product.getUser().getId(), jwtUtil.getUserIdFromToken(request));
        
        productRepository.deleteById(id);
    }

}
