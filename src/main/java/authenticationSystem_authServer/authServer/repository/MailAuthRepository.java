package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.MailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, String> {
    Optional<MailAuth> findByMemberId(String userId);
    @Transactional
    void deleteByMemberId(String memberId);
}
