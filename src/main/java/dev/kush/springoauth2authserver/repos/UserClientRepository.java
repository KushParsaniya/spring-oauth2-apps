package dev.kush.springoauth2authserver.repos;

import dev.kush.springoauth2authserver.models.entities.UserClient;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserClientRepository extends CrudRepository<UserClient, Long> {

    @Query("SELECT client_id FROM user_client WHERE username = :username")
    List<String> getAllClientIdsByUserName(String username);
}
