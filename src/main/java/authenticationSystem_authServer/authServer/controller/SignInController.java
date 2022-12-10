package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import authenticationSystem_authServer.authServer.service.ApiService;
import authenticationSystem_authServer.authServer.service.JwtService;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
@Slf4j
@Controller
@RequestMapping("/signIn")
public class SignInController {

    ApiService apiService;
    MemberService memberService;
    Bcrypt bcrypt;
    JwtService jwtService;

    public SignInController(ApiService apiService, MemberService memberService, Bcrypt bcrypt, JwtService jwtService) {
        this.apiService = apiService;
        this.memberService = memberService;
        this.bcrypt = bcrypt;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody MultiValueMap<String, String> body){
        String password = body.get("password").get(0);
        String userId = body.get("userId").get(0);
        Member member = memberService.findById(userId);

        if(member == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }else{
            if(bcrypt.matching(password,member.getPassword())){
                TokenInfo tokenInfo = memberService.login(userId, password);
                jwtService.login(tokenInfo);
                System.out.println("tokenInfo = " + tokenInfo);
                return new ResponseEntity<>(tokenInfo,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }
        }

    }
}
