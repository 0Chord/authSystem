package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.service.JwtService;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Slf4j
@Controller
public class RefreshTokenController {

    private final JwtService jwtService;

    public RefreshTokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> validateRefreshToken(@RequestBody Map<String, String> body){
        log.info("refresh Controller 실행");
        Map<String, String> map = jwtService.validatedRefreshToken(body.get("refreshToken"));

        if(map.get("status").equals("402")){
            log.info("Refresh Token 기한 만료");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        log.info("Refresh Token 기한 유효");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
