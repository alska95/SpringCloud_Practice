package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void save(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }

    @Override
    public UserEntity find(int userId) {
        UserEntity result = entityManager.find(UserEntity.class, userId);
        return result;
    }

    @Override
    public List<UserEntity> findAll(){
        return entityManager.createQuery("select u from UserEntity u").getResultList();
    }
}
