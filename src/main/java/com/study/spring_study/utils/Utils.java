package com.study.spring_study.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import com.study.spring_study.controller.ListController;
import com.study.spring_study.controller.ProductController;
import com.study.spring_study.dto.CreateAccountDTO;
import com.study.spring_study.dto.ListDTO;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.exception.UnmatchedTokenAndReqIdsException;
import com.study.spring_study.mapper.ModelMapper;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Utils {

    @Autowired
    private ModelMapper mapper;

    public void tokenUserIdEqualToReqUserIdVerification(Long tokenUserId, Long reqUserId){
        if(tokenUserId!=reqUserId) throw new UnmatchedTokenAndReqIdsException();
    }

    public EntityModel<ProductDTO> createProductHateoasModel(ProductDTO dto, HttpServletRequest request){
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id(), request)).withSelfRel());

        return model;
    }
    public EntityModel<ListDTO> createListHateoasModel(ListDTO dto, HttpServletRequest request){
        EntityModel<ListDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ListController.class).findById(dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ListController.class).updateList(mapper.listDtoToCreateListDTO(dto), dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ListController.class).deleteList(dto.id(), request)).withSelfRel());

        return model;
    }

    public boolean areUserCredentialsInvalid(CreateAccountDTO createDTO) {
        return createDTO == null || createDTO.userName() == null || createDTO.fullName() == null ||
            createDTO.password() == null || createDTO.repeatPassword() == null;
    }
}
