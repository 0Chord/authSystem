package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.MailAuth;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.service.MailService;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@RequestMapping("/signup")
@Controller
public class SignupController {
    MemberService memberService;
    Bcrypt bcrypt;
    MailService mailService;

    public SignupController(MemberService memberService, Bcrypt bcrypt, MailService mailService) {
        this.memberService = memberService;
        this.bcrypt = bcrypt;
        this.mailService = mailService;
    }

    private static final String emailRegex =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String phoneRegex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
    private static final String nameRegex = "^[가-힣]*$";

    private static final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{10,20}$";
    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(@RequestBody MultiValueMap<String, String> body){
        Member findMemberObj = memberService.findById(body.get("userId").get(0));
        if(findMemberObj != null){
            System.out.println("findMemberObj = " + findMemberObj);
            return new ResponseEntity<>("StoredMember",HttpStatus.OK);
        }else if(!Pattern.matches(emailRegex,body.get("userId").get(0))){
            return new ResponseEntity<>("WrongIdPattern",HttpStatus.OK);
        }else if(!Pattern.matches(phoneRegex,body.get("phone").get(0))){
            return new ResponseEntity<>("WrongPhonePattern",HttpStatus.OK);
        }else if(!Pattern.matches(nameRegex,body.get("name").get(0))){
            return new ResponseEntity<>("WrongNamePattern",HttpStatus.OK);
        }else if(!Pattern.matches(passwordRegex,body.get("password").get(0))){
            return new ResponseEntity<>("WrongPasswordPattern",HttpStatus.OK);
        }else{
            Member member = new Member();
            member.setUserId(body.get("userId").get(0));
            String encodingPassword = bcrypt.encrypt(body.get("password").get(0));
            member.setPassword(encodingPassword);
            member.setName(body.get("name").get(0));
            member.setNickname(body.get("nickname").get(0));
            member.setPhone(body.get("phone").get(0));
            member.setAdminRight(body.get("adminRight").get(0));
            member.setRoles(Collections.singletonList("ROLE_USER"));
            member.setEmailAuth(false);
            memberService.register(member);
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }

    }

    @PostMapping("/confirmCode")
    public ResponseEntity<?> confirm(@RequestBody MultiValueMap<String, String> body){
        String code = body.get("code").get(0);
        String userId = body.get("userId").get(0);
        MailAuth mailAuth = mailService.findMailAuthByUserId(userId);
        if(Objects.equals(code, mailAuth.getCode())){
            Member member = memberService.findById(userId);
            member.updateEmailAuth(true);
            mailService.deleteMailAuth(userId);
            return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("FAIL",HttpStatus.BAD_REQUEST);
        }
    }
}
