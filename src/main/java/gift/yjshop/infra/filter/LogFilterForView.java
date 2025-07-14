package gift.yjshop.infra.filter;

import gift.dto.MemberRequestDto;
import gift.entity.Member;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.MemberService;
import gift.yjshop.service.AuthServiceJWTandCookie;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogFilterForView implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogFilterForView.class);

    private final MemberService memberService;
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public LogFilterForView(MemberService memberService, AuthServiceJWTandCookie authServiceJWTandCookie){
        this.memberService = memberService;
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[LogFilterForView]필터 등록 완료,,,");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();
        String Method = httpServletRequest.getMethod();

        //로그인 요청에 대해서만 동작하는 필터임...
        if(url.equals("/view/login") && Method.equals("POST")){

            log.info("로그인 필터 is working,,,,");

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            //인증실패예외를 반환하고 이를 Http Response로 렌더링하는 작업이 필요할것 같아요.
            if(email.isBlank() || password.isBlank()){
                throw new MyException(ErrorCode.EMAIL_PASSWORD_REQUIRED);
            }

            //이를 인증할 수 있는 객체(의존성)에게 값을 넘겨야합니다.
            if(!memberService.checkMember(new MemberRequestDto(email, password))){
                throw new MyException(ErrorCode.LOGIN_UNAVAILABLE);
            }

            Member member = memberService.getMemberByEmail(email).get();
            String token = authServiceJWTandCookie.createJwt(email, member.getMemberId(), member.getRole());

            //쿠키 발행 (쿠키에 토큰을 저장)
            Cookie tcookie = new Cookie("yjtoken", token);
            tcookie.setPath("/");
            httpServletResponse.addCookie(tcookie);

            //로그인 성공시
            request.setAttribute("role", member.getRole());
            log.info("쿠키 생성 완료");
        }
        //다음 필터가 있다면 동작해라;
        chain.doFilter(request, response);
    }
}