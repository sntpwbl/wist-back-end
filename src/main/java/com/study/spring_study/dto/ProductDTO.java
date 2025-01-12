package com.study.spring_study.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String picture;
    private boolean bought;
    private List<StoreLinkDTO> links;
}
