package dev.kush.springoauth2authserver.models.dtos;

import java.util.Set;

public record RegisterClientRequest(String clientName, Set<String> redirectUris) {
}
