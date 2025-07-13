package gift.yjshop.infra.filter;

import gift.exception.MyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;

@Component
public class FilterExceptionHandlerForView extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MyException e) {
            logger.info("[FilterExceptionHandlerForView]" + e.getErrorCode());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errormsg", e.getErrorCode().getMessage());
            modelAndView.setViewName("/yjshop/user/loginerror");
            request.setAttribute("errorpage", modelAndView);
            System.out.println("modelAndView = " + modelAndView);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/errortest");
            dispatcher.forward(request, response);
        }
    }
}
