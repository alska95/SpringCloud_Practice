package com.example.userservice.service;

import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserEntity createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));


        log.info(userEntity.getUserId());
        log.info(userEntity.getEmail());
        log.info(userEntity.getEncryptedPwd());
        log.info(userEntity.getName());
        userRepository.save(userEntity);

        return userEntity;
    }

    public List<UserDto> findAllUser(){
        List<UserDto> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        userRepository.findAll().stream().forEach(user ->{
            UserDto target = mapper.map(user , UserDto.class);
            result.add(target);
        });
        return result;
    }
    public UserDto findUser(Long id){
        ModelMapper mapper = new ModelMapper();
        UserDto result = mapper.map(userRepository.find(id) , UserDto.class);
        return result;
    }
}
