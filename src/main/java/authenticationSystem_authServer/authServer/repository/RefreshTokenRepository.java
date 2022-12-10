package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);

    Optional<RefreshToken> findByUserId(String userId);
}
