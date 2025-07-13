package gift.infra;

import gift.entity.Member;
import gift.service.JwtAuthService;
import gift.service.MemberService;
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
public class LoggedInMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(LoggedInMemberArgumentResolver.class);
    private final MemberService memberService;
    private final JwtAuthService jwtAuthService;

    public LoggedInMemberArgumentResolver(MemberService memberService, JwtAuthService jwtAuthService){
        this.memberService = memberService;
        this.jwtAuthService = jwtAuthService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInMember.class)
                && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = httpServletRequest.getHeader("Authorization"); //토큰 꺼내기
        Long id = jwtAuthService.getMemberId(token); //토큰에서 정보 가져오기
        log.info("LoggedInMemberArgumentResolver(memberId = " + id.toString() + ")");
        return memberService.findMember(id).get();
    }
}

