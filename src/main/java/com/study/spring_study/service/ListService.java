package com.study.spring_study.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.CreateListDTO;
import com.study.spring_study.dto.ListDTO;
import com.study.spring_study.exception.NotFoundException;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.ProductList;
import com.study.spring_study.repository.ListRepository;
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
    private ModelMapper mapper;

    @Autowired
    private JwtUtil jwtUtil;

    public ListDTO createList(CreateListDTO dto, HttpServletRequest request){
        if(dto == null || dto.name() == null) throw new NullRequiredObjectException();

        Long userId = jwtUtil.getUserIdFromToken(request);
        ProductList newList = new ProductList(dto.name(), dto.description(), userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found.")));

        return mapper.listToDTO(listRepository.save(newList));
    }

    public List<ListDTO> findAll(){
        return listRepository.findAll().stream().map(l -> {
                return mapper.listToDTO(l);
            }).collect(Collectors.toList());
    }

    public ListDTO findById(Long id, HttpServletRequest request){
        Utils.tokenUserIdEqualToReqUserIdVerification(id, jwtUtil.getUserIdFromToken(request));
        return mapper.listToDTO(listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found.")));
    }
    public ListDTO updateList(CreateListDTO dto, Long id, HttpServletRequest request){
        Utils.tokenUserIdEqualToReqUserIdVerification(id, jwtUtil.getUserIdFromToken(request));
        if(dto == null || dto.name() == null) throw new NullRequiredObjectException();
        ProductList list = listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found."));

        list.setName(dto.name());
        list.setDescription(dto.description() != null ? dto.description() : list.getDescription());

        return mapper.listToDTO(listRepository.save(list));
    }

    public void deleteById(Long id, HttpServletRequest request){
        Utils.tokenUserIdEqualToReqUserIdVerification(id, jwtUtil.getUserIdFromToken(request));
        listRepository.delete(
            listRepository.findById(id).orElseThrow(() -> new NotFoundException("List not found."))
        );
    }


}
