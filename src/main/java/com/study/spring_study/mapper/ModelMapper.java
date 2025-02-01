package com.study.spring_study.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.study.spring_study.dto.CreateListDTO;
import com.study.spring_study.dto.ListDTO;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.StoreLinkDTO;
import com.study.spring_study.dto.UserDTO;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.ProductList;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.model.User;

@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);
    
    @Mapping(source = "username", target = "userName")
    UserDTO userToDTO(User user);
    
    @Mapping(source = "user.id", target = "userId")
    ProductDTO productToDTO(Product p);

    @Mapping(source = "url", target = "url")
    StoreLinkDTO linkToDTO(StoreLink link);

    @Mapping(source = "user.id", target = "userId")
    ListDTO listToDTO(ProductList list);

    @Mapping(source = "name", target = "name")
    CreateListDTO listDtoToCreateListDTO(ListDTO dto);
    
    
}