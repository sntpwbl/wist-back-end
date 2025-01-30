package com.study.spring_study.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import com.study.spring_study.controller.ProductController;
import com.study.spring_study.dto.CreateAccountDTO;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.exception.UnmatchedTokenAndReqIdsException;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {

    public static void tokenUserIdEqualToReqUserIdVerification(Long tokenUserId, Long reqUserId){
        if(tokenUserId!=reqUserId) throw new UnmatchedTokenAndReqIdsException();
    }

    public static EntityModel<ProductDTO> createProductHateoasModel(ProductDTO dto, HttpServletRequest request){
        EntityModel<ProductDTO> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(ProductController.class).findById(dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).updateProduct(dto, dto.id(), request)).withSelfRel());
        model.add(linkTo(methodOn(ProductController.class).deleteProduct(dto.id(), request)).withSelfRel());

        return model;
    }

    public static boolean areUserCredentialsInvalid(CreateAccountDTO createDTO) {
        return createDTO == null || createDTO.userName() == null || createDTO.fullName() == null ||
            createDTO.password() == null || createDTO.repeatPassword() == null;
    }
}
