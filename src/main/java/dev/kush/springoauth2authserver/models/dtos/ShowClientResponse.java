package dev.kush.springoauth2authserver.models.dtos;

import java.util.Set;

public record ShowClientResponse(
        String clientName,
        String clientId,
        Set<String> redirectUris) {
}
