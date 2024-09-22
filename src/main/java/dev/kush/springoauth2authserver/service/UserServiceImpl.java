package dev.kush.springoauth2authserver.service;

import dev.kush.springoauth2authserver.models.dtos.CreateUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;

    private final UserDetailsManager userDetailsManager;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(JwtService jwtService, UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String createUser(CreateUserRequest createUserRequest) {
        if (userDetailsManager.userExists(createUserRequest.username())) {
            throw new IllegalArgumentException("User already exists!");
        }
        UserDetails userDetails = mapToUserDetails(createUserRequest);
        userDetailsManager.createUser(userDetails);
        return jwtService.generateJwtToken(userDetails.getUsername(), getAuthoritiesStringSet(userDetails.getAuthorities()));
    }

    private static Set<String> getAuthoritiesStringSet(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private User mapToUserDetails(CreateUserRequest createUserRequest) {
        return new User(
                createUserRequest.username(),
                passwordEncoder.encode(createUserRequest.password()),
                Set.of(new SimpleGrantedAuthority("SCOPE_USER")));
    }

    @Override
    public String loginUser(CreateUserRequest createUserRequest) {
        var user = userDetailsManager.loadUserByUsername(createUserRequest.username());
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                createUserRequest.username(), createUserRequest.password(), user.getAuthorities());
        try {
            Authentication authenticatedUser = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            return jwtService.generateJwtToken(authenticatedUser.getName(), getAuthoritiesStringSet(authenticatedUser.getAuthorities()));
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Invalid Credentials!");
        }
    }
}
