package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.service.MailService;
import authenticationSystem_authServer.authServer.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/find")
public class FindController {

    MemberService memberService;
    private final MailService mailService;
    Bcrypt bcrypt;

    public FindController(MemberService memberService, MailService mailService, Bcrypt bcrypt) {
        this.memberService = memberService;
        this.mailService = mailService;
        this.bcrypt = bcrypt;
    }

    @PostMapping("/password")
    public ResponseEntity<?> findPassword(@RequestBody MultiValueMap<String,String> body){
        Member member = memberService.findById(body.get("userId").get(0));
        if(member == null){
            return new ResponseEntity<>("NoSearchMember", HttpStatus.OK);
        }else if(!Objects.equals(member.getName(), body.get("name").get(0))){
            return new ResponseEntity<>("DifferentName",HttpStatus.OK);
        }else if(!Objects.equals(member.getPhone(),body.get("phone").get(0))){
            return new ResponseEntity<>("DifferentPhone",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("OK",HttpStatus.OK);
        }
    }

    @PostMapping("/passwordAuth")
    public ResponseEntity<?> mailPassword(@RequestBody MultiValueMap<String,String> body) throws MessagingException, UnsupportedEncodingException {
        String userId = body.get("userId").get(0);
        String authCode = mailService.sendEmail(userId, "passwordAuth");
        String encrypt = bcrypt.encrypt(authCode);
        memberService.updatePassword(userId,encrypt);
        return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
    }
}
