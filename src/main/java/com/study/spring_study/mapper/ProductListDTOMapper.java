package com.study.spring_study.mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.ProductListDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.model.ProductList;

@Component
public class ProductListDTOMapper implements Function<ProductList, ProductListDTO> {
    
    @Override
    public ProductListDTO apply(ProductList list){
        return new ProductListDTO(list.getId(), list.getName(), list.getDescription(), list.getCategories().stream().map(category ->{
            return category;
        }).collect(Collectors.toList()), list.getCreatedAt(), list.getProducts().stream().map(product -> {
            return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPicture(), product.isBought(), product.getStoreLinks().stream().map(link ->{
                return new StoreLinkDTO(link.getStore(), link.getUrl());
            }).collect(Collectors.toList()));
        }).collect(Collectors.toList()));
    }
}
