package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository  {
    void save(UserEntity userEntity);
    Optional<UserEntity> find(String userId);
    List<UserEntity> findAll();
    Optional<UserEntity> findByEmail(String email);
    //crudRepository 사용할 경우 , find를 사용할 경우 select를 추가, 그다음 by 대상 칼럼

}
