package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    private final Greeting greeting;
    private final Environment env;
    public UserController(Environment env, Greeting greeting) {
        this.env = env;
        this.greeting = greeting;
    }

    @GetMapping("/health_check")
    public String status(){
        return "It's working in user service";
    }

    @GetMapping("/welcome")
    public String welcome(){
//        return greeting.getMessage();
        return env.getProperty("greeting.message");
    }

}
