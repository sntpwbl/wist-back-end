package com.study.spring_study.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public record ListDTO(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    Long userId,
    List<ProductDTO> products
) {
    
}
