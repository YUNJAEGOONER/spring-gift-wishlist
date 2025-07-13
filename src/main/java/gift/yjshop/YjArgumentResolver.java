package gift.yjshop;

import gift.entity.Member;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.MemberService;
import gift.yjshop.service.AuthServiceJWTandCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class YjArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(YjArgumentResolver.class);
    private final MemberService memberService;
    private final AuthServiceJWTandCookie authServiceJWTandCookie;

    public YjArgumentResolver(
            MemberService memberService, AuthServiceJWTandCookie authServiceJWTandCookie) {
        this.memberService = memberService;
        this.authServiceJWTandCookie = authServiceJWTandCookie;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(YjUser.class)
                &&parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception
    {
        //쿠키 가져오기
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = null;

        try{
            Cookie[] cookies = servletRequest.getCookies();
            for(Cookie c : cookies){
                if(c.getName().equals("yjtoken")){
                    token = c.getValue();
                }
            }
        }
        catch (NullPointerException e){
            log.info("쿠키가 존재하지 않음");
            throw new MyException(ErrorCode.LOGIN_REQUIRED_FAIL); //로그인 화면으로 유도
        }

        //쿠키에 저장된 토큰으로 부터 사용자 정보 가져오기
        Long id = authServiceJWTandCookie.getMemberId(token);
        return memberService.findMember(id).get();
    }

}
