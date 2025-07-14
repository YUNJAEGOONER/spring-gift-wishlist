package gift.yjshop.service;

import gift.dto.Role;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

//JWT와 관련된 서비스
@Component
public class AuthServiceJWTandCookie {

    private final String secretKey = "ComeOnYouGunnersNorthLondonisRedNorthLondonFOREVER";

    //payload의 정보를 추출하는 함수
    public Long getMemberId(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", Long.class);
    }

    //payload의 정보를 추출하는 함수
    public String getMemberRole(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    //토큰 생성
    public String createJwt(String email, Long memberId, Role role){
        return Jwts.builder()
                .claim("email", email)
                .claim("memberId", memberId)
                .claim("role", role.toString())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    //토큰을 검증 -> 로그인 이후의 동작
    public void checkValidation(String token) {
        try{
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token);
        }
        catch (Exception e){
            throw new MyException(ErrorCode.JWT_VALIDATION_FAIL);
        }
    }

}