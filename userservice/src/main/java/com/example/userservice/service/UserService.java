package com.example.userservice.service;


import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserEntity createUser(UserDto userDto);
    List<UserDto> findAllUser();
    public UserDto findUser(Long id);
}
