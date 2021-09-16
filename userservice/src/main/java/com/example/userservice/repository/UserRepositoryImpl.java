package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void save(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }

    @Override
    public UserEntity find(String userId) {
        UserEntity result = entityManager.createQuery("select u from UserEntity u where u.userId =: userId" , UserEntity.class)
                .setParameter("userId" , userId)
                .getResultList().get(0);
        return result;
    }

    @Override
    public List<UserEntity> findAll(){
        return entityManager.createQuery("select u from UserEntity u").getResultList();
    }

    @Override
    public UserEntity findByEmail(String email) {
        return entityManager.createQuery("select u from UserEntity u where u.email =: email" , UserEntity.class)
                .setParameter("email" , email)
                .getResultList().get(0);
    }
}
