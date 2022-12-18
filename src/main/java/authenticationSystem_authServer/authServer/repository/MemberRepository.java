package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByUserId(String userId);

    List<Member> findAll();

    void deleteByUserId(String userId);
}
