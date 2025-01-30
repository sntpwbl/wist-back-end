package com.study.spring_study.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ListDTO(
    Long id,
    String name,
    String description,
    LocalDateTime createdAt,
    Long userId,
    List<ProductDTO> products
) {
    
}
