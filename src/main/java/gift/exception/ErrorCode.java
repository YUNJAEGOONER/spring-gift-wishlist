package gift.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    JWT_VALIDATION_FAIL(HttpStatus.UNAUTHORIZED, "JWT 인증 실패"),
    LOGIN_REQUIRED_FAIL(HttpStatus.UNAUTHORIZED, "로그인을 해야합니다."),
    EMAIL_PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "아이디와 비밀번호는 필수로 입력해야합니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."),
    UNAVAILABLE_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입되어 있는 이메일 입니다."),
    LOGIN_UNAVAILABLE(HttpStatus.FORBIDDEN, "이메일 또는 비밀번호가 잘못되었습니다.");

    private final HttpStatus statusCode;
    private final String message;

    ErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode(){
        return statusCode;
    }

    public String getMessage(){
        return message;
    }
}
