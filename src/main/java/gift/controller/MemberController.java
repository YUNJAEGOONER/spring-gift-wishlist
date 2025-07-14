package gift.controller;

import gift.dto.JwtResponseDto;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.service.JwtAuthService;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    private final JwtAuthService jwtAuthService;

    public MemberController(MemberService memberService, JwtAuthService jwtAuthService){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
    }

    //회원가입 기능 -> 토큰을 반환
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(
            @RequestBody @Valid MemberRequestDto memberRequestDto,
            HttpServletResponse response
    ){
        Member member = memberService.register(memberRequestDto);
        String token = jwtAuthService.createJwt(member.getEmail(), member.getMemberId(), member.getRole());
        response.addHeader("Authorization", token);
        return new ResponseEntity<>(new JwtResponseDto(token), HttpStatus.CREATED);
    }

    //로그인 기능 -> 토큰을 반환
    @PostMapping("/login")
    public ResponseEntity<Object> login(HttpServletResponse response){
        String token = response.getHeader("Authorization");
        return ResponseEntity.ok().body(new JwtResponseDto(token));
    }

}