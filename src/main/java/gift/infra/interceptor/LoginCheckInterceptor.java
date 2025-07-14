package gift.infra.interceptor;

import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.JwtAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
//로그인이 되어 있는지를 확인하기 위한 인터셉터
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginCheckInterceptor.class);
    private final JwtAuthService jwtAuthService;

    public LoginCheckInterceptor(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    //컨트롤러에 호출 전에 동작
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("로그인 정보를 확인 중,,,");
        log.info("LoginCheckInterceptor.preHandle");

        String token = request.getHeader("Authorization"); //헤더에서 토큰을 꺼내서

        //토큰이 없는 경우
        if(token == null){
            //로그인하는 페이지로 유도
            throw new MyException(ErrorCode.LOGIN_REQUIRED_FAIL);
        }

        //토큰이 존재하는 경우 -> 토큰을 검증
        jwtAuthService.checkValidation(token);
        log.info("JWT 토큰 검증 성공,,,");
        return true;
    }

}