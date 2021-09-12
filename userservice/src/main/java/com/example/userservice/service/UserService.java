package com.example.userservice.service;


import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserEntity createUser(UserDto userDto);
    List<UserDto> findAllUser();
    UserDto findUser(Long id);
    UserDetails loadUserByUsername(String s);
    UserDto getUserDetailsByEmail(String userName);
}
