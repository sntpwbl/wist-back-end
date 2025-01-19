package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.study.spring_study.controller.ProductController;
import com.study.spring_study.controller.ProductListController;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.ProductListDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.mapper.ProductListDTOMapper;
import com.study.spring_study.repository.ProductListRepository;

@Service
public class ProductListService {
    
    @Autowired
    private ProductListRepository repository;
    private final ProductListDTOMapper productListDTOMapper;

    public ProductListService(ProductListDTOMapper productListDTOMapper) {
        this.productListDTOMapper = productListDTOMapper;
    }

    public List<EntityModel<ProductListDTO>> findAllLists(){
        // ProductListDTO dto = productListDTOMapper.apply(repository.findAll());
        List<EntityModel<ProductListDTO>> allLists = repository.findAll().stream().map(list ->{
                ProductListDTO dto = productListDTOMapper.apply(list);
                EntityModel<ProductListDTO> model = EntityModel.of(dto);

                model.add(linkTo(methodOn(ProductListController.class).findById(dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductListController.class).updateList(dto, dto.id())).withSelfRel());
                model.add(linkTo(methodOn(ProductListController.class).deleteList(dto.id())).withSelfRel());

                return model;
            }
        ).collect(Collectors.toList());

        return allLists;
    }

    public EntityModel<ProductListDTO> findById(Long id) {
        ProductListDTO dto = repository.findById(id)
            .map(productListDTOMapper)
            .orElseThrow(() -> new NotFoundException("No product found for this ID."));
        EntityModel<ProductListDTO> model = EntityModel.of(dto);

        model.add(linkTo(methodOn(ProductListController.class).findById(id)).withSelfRel());
        model.add(linkTo(methodOn(ProductListController.class).updateList(dto, id)).withSelfRel());
        model.add(linkTo(methodOn(ProductListController.class).deleteList(id)).withSelfRel());

        return model;
    }


}
