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
//            logger.info("[FilterExceptionHandlerForView]" + e.getErrorCode());
//            request.setAttribute("errormsg", e.getErrorCode().getMessage());
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/login/error");
//            dispatcher.forward(request, response);


            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errormsg", e.getErrorCode().getMessage());
            modelAndView.setViewName("/yjshop/user/loginerror");
            try {
                modelAndView.setView(viewResolver.resolveViewName(modelAndView.getViewName(), Locale.getDefault()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            Map<String, Object> model = modelAndView.getModel();
            View view = modelAndView.getView();
            try {
                view.render(model, request, response);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


        }
    }


}


