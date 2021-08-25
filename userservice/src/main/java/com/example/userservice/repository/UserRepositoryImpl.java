package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void save(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }
}
