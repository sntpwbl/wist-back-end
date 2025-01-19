package com.study.spring_study.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ProductListDTO(
    Long id,
    String name,
    String description,
    List<String> categories,
    LocalDateTime createdAt,
    List<ProductDTO> products
) {
    
}
