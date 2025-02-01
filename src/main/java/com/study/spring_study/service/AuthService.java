package com.study.spring_study.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.AccountCredentialsDTO;
import com.study.spring_study.dto.CreateAccountDTO;
import com.study.spring_study.dto.TokenDTO;
import com.study.spring_study.dto.UserDTO;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.exception.PasswordValidationException;
import com.study.spring_study.exception.SignedUpUsernameException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.Permission;
import com.study.spring_study.model.User;
import com.study.spring_study.repository.PermissionRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtTokenProvider;
import com.study.spring_study.utils.Utils;

@Service
public class AuthService {

    @Autowired
    private final AuthenticationManager authManager;
    
    @Autowired
    private final JwtTokenProvider tokenProvider;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final ModelMapper mapper;

    @Autowired
    private final PermissionRepository permissionRepository;

    @Autowired
    private final Utils utils;
    

    public AuthService(AuthenticationManager authManager, JwtTokenProvider tokenProvider, UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder, ModelMapper mapper, PermissionRepository permissionRepository, Utils utils) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.permissionRepository = permissionRepository;
        this.utils = utils;
    }

    public TokenDTO signIn(AccountCredentialsDTO credentialsDTO){
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken
                (credentialsDTO.userName(), credentialsDTO.password())
            );
            User user = userRepository.findByUsername(credentialsDTO.userName());
            if(user != null){
                return tokenProvider.createAccessTokenObject(user.getId(), user.getUsername(), user.getRoles());

            } else throw new UsernameNotFoundException("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    public UserDTO commonUserSignUp(CreateAccountDTO createDto){
        if(utils.areUserCredentialsInvalid(createDto)){
            throw new NullRequiredObjectException("Required field not sent.");
        } else if(!createDto.password().equals(createDto.repeatPassword()) ){
            throw new PasswordValidationException("Passwords do not match.");
        } else {
            User checkIfUsernameExists = userRepository.findByUsername(createDto.userName());
            if(checkIfUsernameExists != null) throw new SignedUpUsernameException();
            else {
                User newUser = new User();
                Permission permission = permissionRepository.findByDescription("USER");
                newUser.setUserName(createDto.userName());
                newUser.setFullName(createDto.fullName());
                newUser.setPassword(passwordEncoder.encode(createDto.password()));
                newUser.addPermission(permission);

                UserDTO createdUserToDTO = mapper.userToDTO(userRepository.save(newUser));
                return createdUserToDTO;
            }
        }
    }

}
