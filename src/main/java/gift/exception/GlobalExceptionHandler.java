package gift.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //애플리케이션 전역에서 발생하는 예외를 처리하기 위함
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public String handleMyException(MyException e, Model model){

        System.out.println("e.getErrorCode().getMessage() = " + e.getErrorCode().getMessage());
        return null;
//
//        model.addAttribute("errorMsg", e.getErrorCode().getMessage());
//        if(e.getErrorCode().equals(ErrorCode.PRODUCT_NOT_FOUND)) {
//            return "ProoductNotFoun";
//        }
//        if(e.getErrorCode().equals(ErrorCode.MEMBER_NOT_FOUND)){
//            return "members/membernotfound";
//        }
//        if(e.getErrorCode().equals(ErrorCode.JWT_VALIDATION_FAIL)){
//            return "redirect:/view/products/list";
//        }
//
//        return null;
    }

}

