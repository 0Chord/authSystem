package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Slf4j
@Controller
public class AuthController {

    JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/access")
    public ResponseEntity<?> getAccessToken(@RequestBody MultiValueMap<String, String> body){
        String refreshToken = body.get("refreshToken").get(0);
        Map<String, String> validatedRefreshToken = jwtService.validatedRefreshToken(refreshToken);
        return new ResponseEntity<>(validatedRefreshToken.get("accessToken"), HttpStatus.OK);
    }
}
