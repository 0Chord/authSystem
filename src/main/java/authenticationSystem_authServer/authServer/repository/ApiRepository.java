package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.APIKey;

import java.util.Optional;

public interface ApiRepository {
    APIKey save(APIKey apiKey);
    Optional<APIKey> findByDomain(String domain);
}
