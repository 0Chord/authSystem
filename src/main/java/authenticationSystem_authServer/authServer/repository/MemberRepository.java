package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByUserId(String userId);
}
