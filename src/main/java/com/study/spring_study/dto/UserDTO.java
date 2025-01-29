package com.study.spring_study.dto;

import java.util.List;

import com.study.spring_study.model.Permission;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record UserDTO(
    Long id,
    String userName,
    List<Permission> permissions,
    List<ProductDTO> products
) {
    
}
