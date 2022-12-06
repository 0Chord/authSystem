package authenticationSystem_authServer.authServer.repository;

import authenticationSystem_authServer.authServer.domain.APIKey;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class ApiJpaRepository implements ApiRepository{
    EntityManager em;

    public ApiJpaRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public APIKey save(APIKey apiKey) {
        em.persist(apiKey);
        return apiKey;
    }

    @Override
    public Optional<APIKey> findByDomain(String domain) {
        List<APIKey> result = em.createQuery("select a from APIKey a where a.domain = :domain", APIKey.class)
                .setParameter("domain",domain)
                .getResultList();

        return result.stream().findAny();
    }
}
