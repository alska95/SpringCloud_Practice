package com.example.userservice.service;


import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;

public interface UserService {
    UserEntity createUser(UserDto userDto);
}
