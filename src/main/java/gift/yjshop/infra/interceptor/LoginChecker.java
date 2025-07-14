package gift.yjshop.infra.interceptor;

import gift.yjshop.service.AuthServiceJWTandCookie;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginChecker implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginChecker.class);
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public LoginChecker(AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //컨트롤러가 호출되기 전에 실행됨
        log.info("[LoginChecker] preHandle");
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
        return true; //컨트롤러가 동작
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {;
        log.info("[interceptor] postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("[interceptor] afterCompletion");
    }

}