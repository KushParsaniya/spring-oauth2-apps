package dev.kush.springoauth2authserver.models.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_client")
public record UserClient(@Id Long id, String username, String clientId) {
}
