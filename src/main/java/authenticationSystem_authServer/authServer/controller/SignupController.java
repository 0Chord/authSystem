package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Slf4j
@RequestMapping("/signup")
@Controller
public class SignupController {
    MemberService memberService;
    Bcrypt bcrypt;

    public SignupController(MemberService memberService, Bcrypt bcrypt) {
        this.memberService = memberService;
        this.bcrypt = bcrypt;
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(@RequestBody MultiValueMap<String, String> body){
        Member member = new Member();
        member.setUserId(body.get("userId").get(0));
        String encodingPassword = bcrypt.encrypt(body.get("password").get(0));
        member.setPassword(encodingPassword);
        member.setName(body.get("name").get(0));
        member.setNickname(body.get("nickname").get(0));
        member.setPhone(body.get("phone").get(0));
        memberService.register(member);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
