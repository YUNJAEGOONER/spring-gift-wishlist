//package gift.filter;
//
//import gift.dto.MemberRequestDto;
//import gift.entity.Member;
//import gift.exception.ErrorCode;
//import gift.exception.MyException;
//import gift.service.JwtAuthService;
//import gift.service.MemberService;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class LoginFilter implements Filter {
//
//    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
//    private final MemberService memberService;
//    private final JwtAuthService jwtAuthService;
//
//    public LoginFilter(MemberService memberService, JwtAuthService jwtAuthService){
//        this.memberService = memberService;
//        this.jwtAuthService = jwtAuthService;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//        String url = httpServletRequest.getRequestURI();
//        String Method = httpServletRequest.getMethod();
//
//        //로그인 요청에 대해서만 동작하는 필터임...
//        if(url.equals("/api/members/login") && Method.equals("POST")){
//
//            log.info("로그인 요청이 들어옴,,,");
//
//            //TODO: Request 파싱하기 : 실패..
//            String email = "eamil";
//            String password = "password";
//
//            if(email == null || password == null){
//                throw new MyException(ErrorCode.EMAIL_PASSWORD_REQUIRED);
//                //throw new RequiredFieldException("비밀번호와 이메일은 필수로 입력해야 합니다.");
//            }
//
//            if(!memberService.checkMember(new MemberRequestDto(email, password))){
//                throw new MyException(ErrorCode.MEMBER_NOT_FOUND);
//                //throw new MemberNotFoundException("해당하는 회원을 찾을 수 없습니다.");
//            }
//
//            Member member = memberService.getMemberByEmail(email).get();
//            String token = jwtAuthService.createJwt(email, member.getMemberId(), member.getRole());
//
//            httpServletResponse.addHeader("Authorization", "Bearer "+ token);
//            log.info("토큰 생성 완료");
//        }
//        //다음 필터가 있다면 동작해라
//        chain.doFilter(request, response);
//    }
//}
