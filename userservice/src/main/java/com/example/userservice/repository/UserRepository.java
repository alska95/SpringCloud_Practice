package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository  {
    void save(UserEntity userEntity);

}
