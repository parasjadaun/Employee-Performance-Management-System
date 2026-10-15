package com.epms.filter;
import com.epms.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
/** Provides role checks for protected URL areas. */
@WebFilter(urlPatterns = {"/admin/*", "/team/*", "/employee/*"})
public class RoleFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        User user = (User) request.getSession().getAttribute("user");
        String area = request.getRequestURI().substring(request.getContextPath().length()).split("/")[1].toUpperCase();
        if ("TEAM".equals(area)) {
            area = "MANAGER";
        }
        if (user != null && (user.getRole().equals(area) || user.getRole().equals("ADMIN"))) { chain.doFilter(req, res); }
        else { ((HttpServletResponse) res).sendError(HttpServletResponse.SC_FORBIDDEN); }
    }
}
