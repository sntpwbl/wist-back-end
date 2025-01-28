package com.study.spring_study.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.StoreLink;

@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    // @Mapping(source = "userId", target = "userId")
    // ProductDTO productToDTO(Product p);

    @Mapping(source = "url", target = "url")
    StoreLinkDTO linkToDTO(StoreLink link);

    
}