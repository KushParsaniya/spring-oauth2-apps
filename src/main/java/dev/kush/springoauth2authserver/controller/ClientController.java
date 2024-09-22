package dev.kush.springoauth2authserver.controller;

import dev.kush.springoauth2authserver.models.dtos.RegisterClientRequest;
import dev.kush.springoauth2authserver.models.dtos.ResponseDto;
import dev.kush.springoauth2authserver.service.RegisterClientService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final RegisterClientService registerClientService;

    public ClientController(RegisterClientService registerClientService) {
        this.registerClientService = registerClientService;
    }

    @PostMapping("/create")
    public ResponseDto registerClient(Authentication authentication, @RequestBody RegisterClientRequest client) {
        return new ResponseDto(200, registerClientService.registerClient(authentication.getName(), client), "ok");
    }

    @GetMapping("/user")
    public ResponseDto getAllClientsForUser(Authentication authentication) {
        return new ResponseDto(200, registerClientService.getAllClientsForUser(authentication.getName()), "ok");
    }
}
