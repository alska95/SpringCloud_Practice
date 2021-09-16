package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.UserEntity;
import com.example.userservice.dto.UserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    final UserRepository userRepository;
    final BCryptPasswordEncoder passwordEncoder;
    final Environment env;
    final RestTemplate restTemplate;
    final OrderServiceClient orderServiceClient;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, Environment env, RestTemplate restTemplate, OrderServiceClient orderServiceClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.orderServiceClient = orderServiceClient;
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

    public UserDto findUser(String id){
        ModelMapper mapper = new ModelMapper();
        UserDto result = mapper.map(userRepository.find(id) , UserDto.class);
        String orderUrl = String.format(env.getProperty("order_service.url"), id);

        /** restTemplate을 사용해서 서비스간 통신을 하는 방법*/
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//            restTemplate.exchange(orderUrl, HttpMethod.GET, null
//                    , new ParameterizedTypeReference<List<ResponseOrder>>() {
//                    });
//        List<ResponseOrder> orderList = orderListResponse.getBody();

        /** feignClient를 사용해서 서비스간 통신을 하는 방법법*/
//        List<ResponseOrder> orderList = orderServiceClient.getOrders(id);

        /** feign Exception Handling*/
        List<ResponseOrder> orderList = null;
        try{
            orderList = orderServiceClient.getOrders(id);
        }catch (FeignException ex){
            log.error(ex.getMessage());
        }
        result.setOrderList(orderList);
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(s);
        if(userEntity == null)
            throw new UsernameNotFoundException(s);

        return new User(userEntity.getEmail() , userEntity.getEncryptedPwd(),
                true, true,true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String userName) {
        UserEntity byEmail = userRepository.findByEmail(userName);
        if(byEmail == null)
            throw new UsernameNotFoundException(userName);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto result = mapper.map(byEmail, UserDto.class);
        return result;
    }
}
