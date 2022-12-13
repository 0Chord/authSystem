package authenticationSystem_authServer.authServer.service;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import authenticationSystem_authServer.authServer.jwt.JwtTokenProvider;
import authenticationSystem_authServer.authServer.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@Transactional
public class MemberService {
    MemberRepository memberRepository;
    JwtTokenProvider jwtTokenProvider;
    Bcrypt bcrypt;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, Bcrypt bcrypt) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bcrypt = bcrypt;
    }

    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(String userId){
        log.info("userId = " + userId);
        return memberRepository.findByUserId(userId).orElse(null);
    }

    public List<Member> findAllMember(){
        return memberRepository.findAll();
    }
    @Transactional
    public TokenInfo login(String userId, String password){
      Member member = memberRepository.findByUserId(userId).orElseThrow(()->new IllegalArgumentException("NoRegisterId"));
      if(bcrypt.matching(password, member.getPassword())){
          return jwtTokenProvider.createToken(member.getUserId(),member.getRoles(),member.getAdminRight());
      }else{
          throw new IllegalArgumentException("DifferentPassword");
      }
    }

    public void deleteMember(Member member){
        memberRepository.deleteMember(member);
    }
}
