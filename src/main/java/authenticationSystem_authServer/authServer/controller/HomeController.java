package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.domain.APIKey;
import authenticationSystem_authServer.authServer.dto.ApiKeyForm;
import authenticationSystem_authServer.authServer.getApi.CreateApi;
import authenticationSystem_authServer.authServer.service.ApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    CreateApi createApi;
    ApiService apiService;

    public HomeController(CreateApi createApi, ApiService apiService) {
        this.createApi = createApi;
        this.apiService = apiService;
    }

    @GetMapping()
    public String home(){
        return "Home";
    }

    @PostMapping()
    public String enrollApi(Model model, ApiKeyForm apiKeyForm){
        APIKey apiKey = new APIKey();
        apiKey.setDomain(apiKeyForm.getDomain());
        String api = createApi.getApi();
        apiKey.setAPI(api);
        apiService.enroll(apiKey);
        model.addAttribute("apikey", api);
        return "Home";
    }

}
