package gift.yjshop.infra.interceptor;

import gift.dto.Role;
import gift.yjshop.service.AuthServiceJWTandCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminChecker implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AdminChecker.class);
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public AdminChecker(AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //컨트롤러가 호출되기 전에 실행됨
        log.info("[adminchecker] preHandle");
        String token = null;

        try{
            Cookie[] cookies = request.getCookies();
            for(Cookie c : cookies){
                if(c.getName().equals("yjtoken")){
                    token = c.getValue();
                }
            }
        }
        catch (NullPointerException e){
            log.info("쿠키가 존재하지 않음"); //쿠키가 존재하지 않는 경우,,,
            response.sendRedirect("/view/loginform"); //로그인 화면으로 유도
            return false; //컨트롤러가 동작하지 않는다.
        }

        //쿠키에 토큰이 없다면 -> 로그인 페이지로 이동시키기
        if(token == null){
            response.sendRedirect("/view/loginform");
            return false; //컨트롤러가 동작하지 않는다.
        }

        //토큰의 유효성을 확인하기
        log.info("로그인 정보를 확인 중,,,");
        authServiceJWTandCookie.checkValidation(token);
        log.info("JWT 토큰 검증 성공,,,");

        if(Role.valueOf(authServiceJWTandCookie.getMemberRole(token)).equals(Role.ADMIN)){
            log.info("관리자 인증 완료,,,");
            return true; //컨트롤러가 동작
        }

        log.info("일반 사용자 인증 완료,,,");
        return false; //컨트롤러가 동작하지 않음
    }

}