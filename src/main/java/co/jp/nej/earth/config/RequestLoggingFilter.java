package co.jp.nej.earth.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.jp.nej.earth.model.constant.Constant.Session;
import co.jp.nej.earth.util.EStringUtil;
import co.jp.nej.earth.util.LoginUtil;

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
        boolean loginRequest = requestUri.equals(loginURI);

        if (LoginUtil.isLogin(session) || loginRequest || requestUri.contains("/resources/")
                || requestUri.contains("/WS/")) {
            chain.doFilter(request, response);
        } else {
            if (!loginRequest) {
                String lastRequestView = request.getRequestURI().replaceAll(request.getContextPath() + "/",
                        EStringUtil.EMPTY);
                if (!EStringUtil.isEmpty(lastRequestView)) {
                    session.setAttribute(Session.LAST_REQUEST_VIEW, request.getRequestURI());
                }
            }
            response.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }

}
