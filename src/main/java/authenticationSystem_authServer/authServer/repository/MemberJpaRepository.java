package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class MemberJpaRepository implements MemberRepository {

    EntityManager em;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        Member member = em.find(Member.class, userId);
        return Optional.ofNullable(member);
    }

}
