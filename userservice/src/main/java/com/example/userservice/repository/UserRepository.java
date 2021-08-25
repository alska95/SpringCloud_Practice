package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository  {
    void save(UserEntity userEntity);
    UserEntity find(int userId);
    List<UserEntity> findAll();

}
