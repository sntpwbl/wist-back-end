package com.study.spring_study.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.spring_study.dto.AccountCredentialsDTO;
import com.study.spring_study.dto.TokenDTO;
import com.study.spring_study.dto.UserDTO;
import com.study.spring_study.exception.NullRequiredObjectException;
import com.study.spring_study.exception.SignedUpUsernameException;
import com.study.spring_study.mapper.ModelMapper;
import com.study.spring_study.model.Permission;
import com.study.spring_study.model.User;
import com.study.spring_study.repository.PermissionRepository;
import com.study.spring_study.repository.UserRepository;
import com.study.spring_study.security.JwtTokenProvider;

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
    

    public AuthService(AuthenticationManager authManager, JwtTokenProvider tokenProvider, UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder, ModelMapper mapper, PermissionRepository permissionRepository) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.permissionRepository = permissionRepository;
    }

    public TokenDTO signIn(AccountCredentialsDTO credentialsDTO){
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken
                (credentialsDTO.userName(), credentialsDTO.password())
            );
            User user = userRepository.findByUsername(credentialsDTO.userName());
            if(user != null){
                return tokenProvider.createAccessTokenObject(user.getUsername(), user.getRoles());

            } else throw new UsernameNotFoundException("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    public UserDTO commonUserSignUp(AccountCredentialsDTO credentialsDTO){
        try {
            // if(!validateUserCredentials(credentialsDTO)){
            //     throw new NullRequiredObjectException("Username and password fields are both required.");
            // } else {
                User checkIfUsernameExists = userRepository.findByUsername(credentialsDTO.userName());
                if(checkIfUsernameExists != null) throw new SignedUpUsernameException();
                else {
                    User newUser = new User();
                    Permission permission = permissionRepository.findByDescription("USER");
                    newUser.setUserName(credentialsDTO.userName());
                    newUser.setPassword(passwordEncoder.encode(credentialsDTO.password()));
                    newUser.addPermission(permission);

                    UserDTO createdUserToDTO = mapper.userToDTO(userRepository.save(newUser));
                    return createdUserToDTO;
                // }
            }
        } catch (NullRequiredObjectException e) {
            e.printStackTrace();
            throw new NullRequiredObjectException(e.getMessage());
        } catch (SignedUpUsernameException e) {
            e.printStackTrace();
            throw new SignedUpUsernameException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    private boolean validateUserCredentials(AccountCredentialsDTO credentialsDTO) {
        return credentialsDTO == null || credentialsDTO.userName() == null || credentialsDTO.userName().isBlank()
        || credentialsDTO.password() == null || credentialsDTO.password().isBlank();
    }
}
