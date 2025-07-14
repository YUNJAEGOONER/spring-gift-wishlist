package gift.infra.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.JwtAuthService;
import gift.service.MemberService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
    private final MemberService memberService;
    private final JwtAuthService jwtAuthService;
    private final ObjectMapper objectMapper;

    public LoginFilter(MemberService memberService, JwtAuthService jwtAuthService, ObjectMapper objectMapper){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("필터 등록 완료,,,");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();
        String Method = httpServletRequest.getMethod();

        //로그인 요청에 대해서만 동작하는 필터임...
        if(url.equals("/api/members/login") && Method.equals("POST")){

            log.info("로그인 필터 is working,,,,");

            BufferedReader inputStream = httpServletRequest.getReader();
            MemberRequestDto memberRequestDto = objectMapper.readValue(inputStream, MemberRequestDto.class);

            //HttpServletRequest에서 인증정보를 파싱하고
            String email = memberRequestDto.email();
            String password = memberRequestDto.password();

            //인증실패예외를 반환하고 이를 Http Response로 렌더링하는 작업이 필요할것 같아요.
            if(email == null || password == null){
                throw new MyException(ErrorCode.EMAIL_PASSWORD_REQUIRED);
            }

            //이를 인증할 수 있는 객체(의존성)에게 값을 넘겨야합니다.
            if(!memberService.checkMember(new MemberRequestDto(email, password))){
                throw new MyException(ErrorCode.LOGIN_UNAVAILABLE);
            }

            Member member = memberService.getMemberByEmail(email).get();
            String token = jwtAuthService.createJwt(email, member.getMemberId(), member.getRole());

            httpServletResponse.addHeader("Authorization", token);
            log.info("토큰 생성 완료");
        }
        //다음 필터가 있다면 동작해라;
        chain.doFilter(request, response);
    }
}