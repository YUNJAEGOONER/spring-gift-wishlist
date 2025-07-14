package gift.service;

import gift.entity.Member;
import gift.dto.MemberRequestDto;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.repository.MemberRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //멤버 회원 가입 -> 리포지토리에 저장
    @Transactional
    public Member register(MemberRequestDto memberRequestDto){

        //중복을 확인 - memberService내에서 이미 등록된 메일이라면 예외를 던져서 예외처리로 HttpRepsonse를 내는 방식이 좋을것 같아요
        if(getMemberByEmail(memberRequestDto.email()).isPresent()){ //중복이라면,,,
            throw new MyException(ErrorCode.UNAVAILABLE_EMAIL);
        }
        //중복된 이메일이 아니라면 회원가입을 진행
        Member member = new Member(memberRequestDto.email(), memberRequestDto.password());
        Member createdMemeber = memberRepository.addMember(member);
        return createdMemeber;
    }

    //로그인 기능 -> 이메일과 비밀번호가 일치하는지 확인하는 로직
    public Boolean checkMember(@Valid MemberRequestDto memberRequestDto){
        Optional<Member> member = memberRepository.findMemberByEmailAndPassword(memberRequestDto.email(), memberRequestDto.password());
        if(member.isEmpty()){
            return false;
        }
        return true;
    }

    //특정 멤버를 조회하는 기능
    public Optional<Member> findMember(Long id){
        return memberRepository.findMemberById(id);
    }

    public Optional<Member> getMemberByEmail(String email){
        return memberRepository.findMemberByEmail(email);
    }

    //멤버의 정보 수정이 가능한지 확인하는 기능
    public boolean checkAvailableModify(Long id, MemberRequestDto memberRequestDto){
        Optional<Member> member = memberRepository.findMemberByEmail(memberRequestDto.email());

        //이메일을 변경하는 경우 (이메일 + 비밀번호 모두 변경)
        if(member.isEmpty()){
            return true; //변경 반영
        }

        //비밀번호만 변경하는 경우(이메일은 변경하지 않음)
        String email = memberRepository.findMemberById(id).get().getEmail();
        if(email.equals(memberRequestDto.email())){
            return true; //변경 반영
        }

        return false; //변경 못함 -> 이메일 중복이 발생
    }

    //멤버의 정보를 수정하는 기능
    public void modifyMember(Long id, MemberRequestDto memberRequestDto){
        Member member = new Member(memberRequestDto.email(), memberRequestDto.password());
        memberRepository.modifyMember(id, member);
    }

    //멤버를 삭제하는 기능
    public void removeMember(Long id){
        memberRepository.removeMemberById(id);
    }

    public List<Member> getAllMembers(){
        return memberRepository.findAllMember();
    }

}