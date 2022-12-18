package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.MailAuth;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.domain.RefreshToken;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import authenticationSystem_authServer.authServer.jwt.JwtTokenProvider;
import authenticationSystem_authServer.authServer.service.ApiService;
import authenticationSystem_authServer.authServer.service.JwtService;
import authenticationSystem_authServer.authServer.service.MailService;
import authenticationSystem_authServer.authServer.service.MemberService;
import jakarta.mail.MessagingException;
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

import java.io.UnsupportedEncodingException;
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
    private final MemberService memberService;
    Bcrypt bcrypt;
    JwtService jwtService;
    HttpHeaders httpHeaders;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;

    public SignInController(ApiService apiService, MemberService memberService, Bcrypt bcrypt, JwtService jwtService, HttpHeaders httpHeaders, JwtTokenProvider jwtTokenProvider, MailService mailService) {
        this.apiService = apiService;
        this.memberService = memberService;
        this.bcrypt = bcrypt;
        this.jwtService = jwtService;
        this.httpHeaders = httpHeaders;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MultiValueMap<String, String> body) throws URISyntaxException {
        String password = body.get("password").get(0);
        String userId = body.get("userId").get(0);
        Member member = memberService.findById(userId);
        System.out.println("member = " + member);
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
        memberService.deleteMember(userId);
        List<Member> members = memberService.findAllMember();
        return new ResponseEntity<>(members,HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> findAdmin(@RequestBody MultiValueMap<String, String> body){
        String accessToken = body.get("accessToken").get(0);
        String admin = jwtService.getAdmin(accessToken);
        return new ResponseEntity<>(admin,HttpStatus.OK);
    }

    @PostMapping("/mailAuth")
    public ResponseEntity<?> mailConfirm(@RequestBody MultiValueMap<String, String> body) throws MessagingException, UnsupportedEncodingException{
        String userId = body.get("userId").get(0);
        String authCode = mailService.sendEmail(userId);
        MailAuth mailAuth = new MailAuth();
        mailAuth.setMemberId(userId);
        mailAuth.setCode(authCode);
        mailService.saveCode(mailAuth);
        return new ResponseEntity<>(authCode,HttpStatus.OK);
    }
}
