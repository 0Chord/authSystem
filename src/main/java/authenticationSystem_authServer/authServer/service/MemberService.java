package authenticationSystem_authServer.authServer.service;

import authenticationSystem_authServer.authServer.bcrypt.Bcrypt;
import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.dto.TokenInfo;
import authenticationSystem_authServer.authServer.jwt.JwtTokenProvider;
import authenticationSystem_authServer.authServer.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void register(Member member) {
        memberRepository.save(member);
    }

    public Member findById(String userId){
        return memberRepository.findByUserId(userId).orElse(null);
    }

    public List<Member> findAllMember(){
        return memberRepository.findAll();
    }
    @Transactional
    public TokenInfo login(String userId, String password){
      Member member = memberRepository.findByUserId(userId).orElseThrow(()->new IllegalArgumentException("NoRegisterId"));
      if(bcrypt.matching(password, member.getPassword())){
          return jwtTokenProvider.createToken(member.getUserId(),member.getRoles());
      }else{
          throw new IllegalArgumentException("DifferentPassword");
      }
    }

    @Transactional
    public void updatePassword(String userId, String password){
        Member member = findById(userId);
        member.updatePassword(password);
    }
    public void deleteMember(String userId){
        memberRepository.deleteByUserId(userId);
    }
}
