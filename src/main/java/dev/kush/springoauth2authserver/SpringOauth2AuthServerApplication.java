package dev.kush.springoauth2authserver;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SpringOauth2AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringOauth2AuthServerApplication.class, args);
    }

//    @Bean
    ApplicationRunner applicationRunner(
            RegisteredClientRepository registeredClientRepository,
            UserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder) {
        return args -> {

            RegisteredClient r1 = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("client-1")
                    .clientSecret(passwordEncoder.encode("secret-1"))
                    .clientName("Demo Spring client")
                    .authorizationGrantTypes(authorizationGrantTypes -> {
                        authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    })
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .redirectUri("https://spring.io")
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .tokenSettings(
                            TokenSettings.builder().build()
                    )
                    .build();

            registeredClientRepository.save(r1);

            UserDetails u1 = new User("kush", passwordEncoder.encode("1234"), List.of(new SimpleGrantedAuthority("SCOPE_ADMIN")));

            userDetailsManager.createUser(u1);
        };
    }
}
