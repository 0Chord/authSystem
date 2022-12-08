package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.APIKey;
import authenticationSystem_authServer.authServer.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
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
        List<Member> result = em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId",userId)
                .getResultList();
        return result.stream().findAny();
    }

}
