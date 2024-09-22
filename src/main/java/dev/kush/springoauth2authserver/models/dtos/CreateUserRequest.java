package dev.kush.springoauth2authserver.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@Email(message = "Please Enter valid email") String username,
                                @Size(min = 8, message = "Password size must be 8 or greater") String password) {
}
