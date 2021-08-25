package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final UserService userService;

//    private final Greeting greeting;
    private final Environment env;
    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
//        this.greeting = greeting;
        this.userService = userService;
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

    @PostMapping("/user")
    public String createUser(@RequestBody RequestUser requestUser){
        UserDto userDto = new UserDto();
        userDto.setPwd(requestUser.getPwd());
        userDto.setName(requestUser.getName());
        userDto.setEmail(requestUser.getEmail());

        userService.createUser(userDto);
        return "Create user method is called";
}

}
