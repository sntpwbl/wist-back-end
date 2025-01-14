package com.study.spring_study.mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.model.Product;

public class ProductDTOMapper implements Function<Product, ProductDTO> {

    @Override
    public ProductDTO apply(Product product){
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPicture(), product.isBought(), product.getLinks()
        .stream()
        .map(link -> {
            return new StoreLinkDTO(link.getStore(), link.getUrl());
            }).collect(Collectors.toList()));
    }

}
    

