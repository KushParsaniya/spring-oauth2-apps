package dev.kush.springoauth2authserver.service;

import dev.kush.springoauth2authserver.models.dtos.RegisterClientRequest;
import dev.kush.springoauth2authserver.models.dtos.RegisterClientResponse;
import dev.kush.springoauth2authserver.models.dtos.ShowClientResponse;
import dev.kush.springoauth2authserver.models.entities.UserClient;
import dev.kush.springoauth2authserver.repos.UserClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class RegisterClientServiceImpl implements RegisterClientService {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserClientRepository userClientRepository;

    public RegisterClientServiceImpl(RegisteredClientRepository registeredClientRepository, PasswordEncoder passwordEncoder, UserClientRepository userClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
        this.passwordEncoder = passwordEncoder;
        this.userClientRepository = userClientRepository;
    }

    @Override
    public RegisterClientResponse registerClient(String username, RegisterClientRequest client) {
        String clientId = UUID.randomUUID().toString();
        String clientSecret = UUID.randomUUID().toString();
        RegisteredClient registeredClient = mapToRegisteredClient(client, clientId, clientSecret);
        registeredClientRepository.save(registeredClient);
        // TODO: use security context to retrieve name of user and add it to user_client table
        userClientRepository.save(new UserClient(null, username, registeredClient.getId()));
        return new RegisterClientResponse(clientId, clientSecret);
    }

    @Override
    public List<ShowClientResponse> getAllClientsForUser(String username) {
        List<String> ids = userClientRepository.getAllClientIdsByUserName(username);
        return ids.stream()
                .map(registeredClientRepository::findById)
                .filter(Objects::nonNull)
                .map(this::mapToShowClientResponse)
                .toList();
    }

    private ShowClientResponse mapToShowClientResponse(RegisteredClient registeredClient) {
        return new ShowClientResponse(registeredClient.getClientName(), registeredClient.getClientId(), registeredClient.getRedirectUris());
    }

    private RegisteredClient mapToRegisteredClient(RegisterClientRequest client, String clientId, String clientSecret) {
        return RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .clientName(client.clientName())
                .redirectUris((redirectUris) -> redirectUris.addAll(client.redirectUris()))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientIdIssuedAt(Instant.now())
                .clientSecretExpiresAt(LocalDateTime.now().plusYears(100).toInstant(ZoneOffset.UTC))
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder().build())
                .build();
    }
}
