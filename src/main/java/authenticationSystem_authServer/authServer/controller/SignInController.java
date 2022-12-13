package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.domain.RefreshToken;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import authenticationSystem_authServer.authServer.jwt.JwtTokenProvider;
import authenticationSystem_authServer.authServer.service.ApiService;
import authenticationSystem_authServer.authServer.service.JwtService;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/signIn")
public class SignInController {

    ApiService apiService;
    MemberService memberService;
    Bcrypt bcrypt;
    JwtService jwtService;
    HttpHeaders httpHeaders;
    JwtTokenProvider jwtTokenProvider;

    public SignInController(ApiService apiService, MemberService memberService, Bcrypt bcrypt, JwtService jwtService, HttpHeaders httpHeaders, JwtTokenProvider jwtTokenProvider) {
        this.apiService = apiService;
        this.memberService = memberService;
        this.bcrypt = bcrypt;
        this.jwtService = jwtService;
        this.httpHeaders = httpHeaders;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MultiValueMap<String, String> body) throws URISyntaxException {
        String password = body.get("password").get(0);
        String userId = body.get("userId").get(0);
        Member member = memberService.findById(userId);

        if (member == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            if (bcrypt.matching(password, member.getPassword())) {
                TokenInfo tokenInfo = memberService.login(userId, password);
                jwtService.login(tokenInfo);
                return new ResponseEntity<>(tokenInfo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> test(@RequestBody MultiValueMap<String, String> body) {
        if (body.get("accessToken")!=null) {
            boolean accessToken = jwtTokenProvider.validateToken(body.get("accessToken").get(0));
            return new ResponseEntity<>(accessToken,HttpStatus.OK);
        }
        Map<String, String> refreshToken = jwtService.validatedRefreshToken(body.get("refreshToken").get(0));
        return new ResponseEntity<>(refreshToken, HttpStatus.OK);
    }

    @PostMapping("/member")
    public ResponseEntity<?> getMember(@RequestBody MultiValueMap<String, String> body){
        String refreshToken = body.get("refreshToken").get(0);
        RefreshToken token = jwtService.getRefreshToken(refreshToken).get();
        Member member = memberService.findById(token.getUserId());
        return new ResponseEntity<>(member,HttpStatus.OK);
    }

    @PostMapping("/manage")
    public ResponseEntity<?> getMembers(@RequestBody MultiValueMap<String, String> body){
        List<Member> allMember = memberService.findAllMember();
        return new ResponseEntity<>(allMember,HttpStatus.OK);
    }

    @PostMapping("/removeMember")
    public ResponseEntity<?> removeMember(@RequestBody MultiValueMap<String, String> body){
        String userId = body.get("userId").get(0);
        Member member = memberService.findById(userId);
        memberService.deleteMember(member);
        List<Member> members = memberService.findAllMember();
        return new ResponseEntity<>(members,HttpStatus.OK);
    }
}
