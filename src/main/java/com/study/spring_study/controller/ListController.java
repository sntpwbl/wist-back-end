package com.study.spring_study.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.spring_study.dto.CreateListDTO;
import com.study.spring_study.dto.ListDTO;
import com.study.spring_study.service.ListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/list")
@Tag(name = "List", description = "Manages lists operations")
public class ListController {
    @Autowired
    private ListService listService;

    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Creates a list", description = "Creates a list with name and description.", tags={"List"}, responses = {
        @ApiResponse(description= "Created", responseCode = "201", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListDTO.class)))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> createList(@RequestBody CreateListDTO dto, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listService.createList(dto, request));
    }
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Returns a list", description = "Returns a list by its ID.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Forbidden access", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> findList(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(listService.findById(id, request));
    }
    @GetMapping("/user")
    @Operation(summary = "Returns a users' lists", description = "Returns all created and storaged lists. Does not return XML.", tags={"Product"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListDTO.class)))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<List<ListDTO>> getUserLists(HttpServletRequest request) {
        return ResponseEntity.ok(listService.findListsByUserId(request));
    }
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Updates a list", description = "Updates a list by its ID.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Forbidden access", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> updateList(@RequestBody CreateListDTO dto, Long id, HttpServletRequest request) {
        return ResponseEntity.ok(listService.updateList(dto, id, request));
    }
    @PatchMapping("/add/{productId}/{listId}")
    @Operation(summary = "Adds a product to a list", description = "Adds a product to a list.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Forbidden access", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> addProductToList(@PathVariable Long productId, @PathVariable Long listId, HttpServletRequest request){
        return ResponseEntity.ok(listService.addProductToList(productId, listId, request));
    }
    @PatchMapping("/remove/{productId}/{listId}")
    @Operation(summary = "Removes a product to a list", description = "Removes a product to a list.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListDTO.class))),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Forbidden access", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> removeProductToList(@PathVariable Long productId, @PathVariable Long listId, HttpServletRequest request){
        return ResponseEntity.ok(listService.removeProductFromList(productId, listId, request));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a list", description = "Deletes a list by its ID.", tags={"List"}, responses = {
        @ApiResponse(description= "Success", responseCode = "200", content = @Content),
        @ApiResponse(description= "Bad request", responseCode = "400", content = @Content),
        @ApiResponse(description= "Forbidden access", responseCode = "403", content = @Content),
        @ApiResponse(description= "Internal server error", responseCode = "500", content = @Content)
    })
    public ResponseEntity<ListDTO> findList(@RequestBody CreateListDTO dto, Long id, HttpServletRequest request) {
        return ResponseEntity.ok(listService.updateList(dto, id, request));
    }
    
}
