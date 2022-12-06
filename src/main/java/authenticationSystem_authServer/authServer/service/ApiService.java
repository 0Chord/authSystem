package authenticationSystem_authServer.authServer.service;

import authenticationSystem_authServer.authServer.domain.APIKey;
import authenticationSystem_authServer.authServer.repository.ApiRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ApiService {

    ApiRepository apiRepository;

    public ApiService(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public void enroll(APIKey apiKey){
        apiRepository.save(apiKey);
    }

    public APIKey findByDomain(String domain){
        if(apiRepository.findByDomain(domain).isPresent()){
            return apiRepository.findByDomain(domain).get();
        }else{
            return null;
        }
    }
}
