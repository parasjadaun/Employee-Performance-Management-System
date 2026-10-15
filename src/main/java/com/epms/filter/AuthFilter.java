package com.epms.filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
/** Blocks requests that do not have an authenticated session. */
@WebFilter("/*")
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.equals("/login") || path.equals("/health") || path.startsWith("/assets/")) { chain.doFilter(req, res); return; }
        if (request.getSession(false) == null || request.getSession(false).getAttribute("user") == null) {
            ((HttpServletResponse) res).sendRedirect(request.getContextPath() + "/login"); return;
        }
        chain.doFilter(req, res);
    }
}
