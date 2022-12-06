package authenticationSystem_authServer.authServer.controller;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.dto.MemberForm;
import authenticationSystem_authServer.authServer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String enroll(@Validated MemberForm memberForm){
        System.out.println("=========================");
        System.out.println("API 통신 성공");
        System.out.println("===========================");
//        Member member = new Member();
//        if(memberForm.getPassword().length()>15){
//            System.out.println("비밀번호 초과 오류!!!!");
//        }
//        if(Objects.equals(memberForm.getPassword(), memberForm.getAuthPassword())){
//            String encryptPassword = bcrypt.encrypt(memberForm.getPassword());
//            member.setUserId(memberForm.getUserId());
//            member.setName(memberForm.getName());
//            member.setPassword(encryptPassword);
//            member.setPhone(memberForm.getPhone());
//            member.setNickname(memberForm.getNickname());
//            memberService.register(member);
//            return "http://localhost:8080/";
//        }
//        System.out.println("memberForm.getUserId() = " + memberForm.getUserId());
//        System.out.println("memberForm.getPassword() = " + memberForm.getPassword());
//        System.out.println("memberForm.getAuthPassword() = " + memberForm.getAuthPassword());
//        System.out.println("memberForm.getName() = " + memberForm.getName());
//        System.out.println("memberForm.getNickname() = " + memberForm.getNickname());

        return null;
    }
}
