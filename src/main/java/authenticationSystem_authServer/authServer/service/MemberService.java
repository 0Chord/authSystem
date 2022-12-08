package authenticationSystem_authServer.authServer.service;

import authenticationSystem_authServer.authServer.domain.Member;
import authenticationSystem_authServer.authServer.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(String userId){
        return memberRepository.findByUserId(userId).orElse(null);
    }
}
