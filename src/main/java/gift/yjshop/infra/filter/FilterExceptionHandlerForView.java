package gift.yjshop.infra.filter;

import gift.exception.MyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

@Component
public class FilterExceptionHandlerForView extends OncePerRequestFilter {

    @Autowired private ViewResolver viewResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MyException e) {
            logger.info("[FilterExceptionHandlerForView]" + e.getErrorCode());
            request.setAttribute("errormsg", e.getErrorCode().getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/login/error");
            dispatcher.forward(request, response);
        }
    }


}