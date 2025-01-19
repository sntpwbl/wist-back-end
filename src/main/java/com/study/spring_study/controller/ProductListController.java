package com.study.spring_study.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.ListWithoutProductsDTO;
import com.study.spring_study.dto.ProductDTO;
import com.study.spring_study.dto.ProductListDTO;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.ProductList;
import com.study.spring_study.model.StoreLink;
import com.study.spring_study.service.ProductListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/list")
@Tag(name = "Lists", description = "Used to manage lists operations")
public class ProductListController {
    @Autowired
    private ProductListService service;

    @GetMapping("/find-all")
    @Operation(summary = "Returns all created lists", description = "Returns all created and storaged lists.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductListDTO.class)))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<EntityModel<ProductListDTO>>> findAllLists(){
        return ResponseEntity.ok(service.findAllLists());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns one single list", description = "Returns one single list by its identification.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductListDTO>> findById(@PathVariable Long id) {
        EntityModel<ProductListDTO> model = service.findById(id);
        return ResponseEntity.ok().body(model);
    }

    @PostMapping("/create")
    @Operation(summary = "Creates one single list", description = "Creates one single list by passing all of the required fields.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductListDTO>> createList(@RequestBody ListWithoutProductsDTO dto) {
        ProductList list = new ProductList();
        list.setName(dto.name());
        list.setDescription(dto.description());
        list.setCategories(dto.categories().stream().map(category ->{
            return category;
        }).collect(Collectors.toList()));
        list.setCreatedAt(LocalDateTime.now());
        list.setProducts(new ArrayList<>());


        EntityModel<ProductListDTO> savedList = service.createList(list);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Updates one single list", description = "Updates one single list using its identification and all of the required fields.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<EntityModel<ProductListDTO>> updateList(@RequestBody ProductListDTO dto, @PathVariable Long id) {
        EntityModel<ProductListDTO> updatedList = service.updateList(dto, id);
        return ResponseEntity.ok().body(updatedList);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletes one single list", description = "Deletes one single list by using its identification.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "204", content = @Content),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description= "Not found", responseCode = "404", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<String> deleteList(@PathVariable Long id){
        service.deleteList(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
