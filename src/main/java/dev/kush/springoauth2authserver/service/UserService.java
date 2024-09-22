package dev.kush.springoauth2authserver.service;

import dev.kush.springoauth2authserver.models.dtos.CreateUserRequest;

public interface UserService {
    String createUser(CreateUserRequest createUserRequest);

    String loginUser(CreateUserRequest createUserRequest);
}
