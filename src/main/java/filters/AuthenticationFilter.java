package filters;

import controllers.LoginController;
import controllers.URLs;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    public static final List<String> PROTECTED_URLS = List.of(
            URLs.CALENDAR,
            URLs.USERS,
            URLs.LOGOUT,
            URLs.HOME
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(LoginController.USER_SESSION_ATTRIBUTE) == null) {
            for (String pUrl : PROTECTED_URLS) {
                if (path.matches(pUrl)) {
                    resp.sendRedirect(URLs.LOGIN);
                    return;
                }
            }
        }
        filterChain.doFilter(servletRequest, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

