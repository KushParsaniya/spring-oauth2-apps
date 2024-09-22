package dev.kush.springoauth2authserver.service;

import java.util.Set;

public interface JwtService {

    String generateJwtToken(String username, Set<String> authorities);
}
