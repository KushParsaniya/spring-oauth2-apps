package dev.kush.springoauth2authserver.controller;

import dev.kush.springoauth2authserver.models.dtos.CreateUserRequest;
import dev.kush.springoauth2authserver.models.dtos.ResponseDto;
import dev.kush.springoauth2authserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseDto createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return new ResponseDto(201, userService.createUser(createUserRequest), "User created successfully");
    }

    @PostMapping("/login")
    public ResponseDto loginUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return new ResponseDto(200, userService.loginUser(createUserRequest), "User logged in successfully");
    }

    @GetMapping()
    public String hello() {
        return "Hello from User Controller";
    }
}
