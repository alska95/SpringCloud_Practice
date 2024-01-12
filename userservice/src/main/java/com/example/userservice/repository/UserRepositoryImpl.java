package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUserId(String userId) {
        return entityManager.createQuery("select u from UserEntity u where u.userId =: userId" , UserEntity.class)
                .setParameter("userId" , userId)
                .getResultStream().findFirst();
    }

    @Override
    public List<UserEntity> findAll(){
        return entityManager.createQuery("select u from UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return entityManager.createQuery("select u from UserEntity u where u.email =: email" , UserEntity.class)
                .setParameter("email" , email)
                .getResultStream().findFirst();
    }
}
