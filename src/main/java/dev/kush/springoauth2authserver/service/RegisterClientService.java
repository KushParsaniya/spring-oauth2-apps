package dev.kush.springoauth2authserver.service;

import dev.kush.springoauth2authserver.models.dtos.RegisterClientRequest;
import dev.kush.springoauth2authserver.models.dtos.RegisterClientResponse;
import dev.kush.springoauth2authserver.models.dtos.ShowClientResponse;

import java.util.List;

public interface RegisterClientService {

    RegisterClientResponse registerClient(String username, RegisterClientRequest client);

    List<ShowClientResponse> getAllClientsForUser(String name);
}
