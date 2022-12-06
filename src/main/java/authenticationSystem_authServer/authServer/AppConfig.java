package authenticationSystem_authServer.authServer;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.bcrypt.BcryptVersion1;
import authenticationSystem_authServer.authServer.getApi.CreateApi;
import authenticationSystem_authServer.authServer.getApi.CreateApiVer1;
import authenticationSystem_authServer.authServer.repository.ApiJpaRepository;
import authenticationSystem_authServer.authServer.repository.ApiRepository;
import authenticationSystem_authServer.authServer.repository.MemberJpaRepository;
import authenticationSystem_authServer.authServer.repository.MemberRepository;
import authenticationSystem_authServer.authServer.service.ApiService;
import authenticationSystem_authServer.authServer.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final EntityManager em;

    @Autowired
    public AppConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemberJpaRepository(em);
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public ApiRepository apiRepository(){
        return new ApiJpaRepository(em);
    }

    @Bean
    public ApiService apiService(){
        return new ApiService(apiRepository());
    }

    @Bean
    public Bcrypt bcrypt(){ return new BcryptVersion1();}

    @Bean
    public CreateApi createApi(){ return new CreateApiVer1();
    }
}
