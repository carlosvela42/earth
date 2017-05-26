package co.jp.nej.earth.config;

import co.jp.nej.earth.model.constant.Constant.*;
import co.jp.nej.earth.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;

@WebFilter("/*")
public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        String loginURI = request.getContextPath() + "/login";
        String requestUri = request.getRequestURI();

        if (LoginUtil.isLogin(session) || requestUri.equals(loginURI) || requestUri.contains("/resources/")
                || requestUri.contains("/WS/")) {
            chain.doFilter(request, response);
        } else {
            String lastRequestView = request.getRequestURI().replaceAll(request.getContextPath() + "//",
                    EStringUtil.EMPTY);
            if (!EStringUtil.isEmpty(lastRequestView)) {
                session.setAttribute(Session.LAST_REQUEST_VIEW, lastRequestView);
            }

            response.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }

}
