package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.CreateListDTO;
import com.study.spring_study.dto.ListDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.Product;
import com.study.spring_study.model.ProductList;
import com.study.spring_study.repository.ListRepository;
import com.study.spring_study.repository.ProductRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtUtil;
import com.study.spring_study.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ListService {
    
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Utils utils;

    public EntityModel<ListDTO> createList(CreateListDTO dto, HttpServletRequest request){
        if(dto == null || dto.name() == null) throw new NullRequiredObjectException();

        Long userId = jwtUtil.getUserIdFromToken(request);
        ProductList newList = new ProductList(dto.name(), dto.description(), userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found.")));

        return utils.createListHateoasModel(mapper.listToDTO(listRepository.save(newList)), request);
    }

    public List<ListDTO> findAll(){
        return listRepository.findAll().stream().map(l -> {
                return mapper.listToDTO(l);
            }).collect(Collectors.toList());
    }

    public EntityModel<ListDTO> findById(Long id, HttpServletRequest request){
        ProductList list = listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found."));
        utils.tokenUserIdEqualToReqUserIdVerification(list.getUser().getId(), jwtUtil.getUserIdFromToken(request));

        return utils.createListHateoasModel(mapper.listToDTO(list), request);
    }

    public List<EntityModel<ListDTO>> findListsByUserId(HttpServletRequest request){
        Long userId = jwtUtil.getUserIdFromToken(request);
        return listRepository.findByUserId(userId).stream().map(l -> {
            return utils.createListHateoasModel(mapper.listToDTO(l), request);
        }).collect(Collectors.toList());

    }
    public EntityModel<ListDTO> updateList(CreateListDTO dto, Long id, HttpServletRequest request){
        if(dto == null || dto.name() == null) throw new NullRequiredObjectException();

        ProductList list = listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found."));
        utils.tokenUserIdEqualToReqUserIdVerification(list.getUser().getId(), jwtUtil.getUserIdFromToken(request));
        
        list.setName(dto.name());
        list.setDescription(dto.description() != null ? dto.description() : list.getDescription());

        return utils.createListHateoasModel(mapper.listToDTO(listRepository.save(list)), request);
    }
    public EntityModel<ListDTO> addProductToList(Long productId, Long listId, HttpServletRequest request){
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("List not found."));
        ProductList list = listRepository.findById(listId).orElseThrow(() -> new NotFoundException("List not found."));

        utils.tokenUserIdEqualToReqUserIdVerification(list.getUser().getId(), jwtUtil.getUserIdFromToken(request));
        list.addProduct(product);

        return utils.createListHateoasModel(mapper.listToDTO(listRepository.save(list)), request);

    }
    public EntityModel<ListDTO> removeProductFromList(Long productId, Long listId, HttpServletRequest request){
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("List not found."));
        ProductList list = listRepository.findById(listId).orElseThrow(() -> new NotFoundException("List not found."));

        utils.tokenUserIdEqualToReqUserIdVerification(list.getUser().getId(), jwtUtil.getUserIdFromToken(request));
        list.removeProduct(product);

        return utils.createListHateoasModel(mapper.listToDTO(listRepository.save(list)), request);

    }
    public void deleteById(Long id, HttpServletRequest request){
        ProductList list = listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found."));
        utils.tokenUserIdEqualToReqUserIdVerification(list.getUser().getId(), jwtUtil.getUserIdFromToken(request));

        listRepository.delete(list);
    }


}
