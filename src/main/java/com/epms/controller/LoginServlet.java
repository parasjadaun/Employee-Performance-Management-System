package com.epms.controller;
import com.epms.exception.*;
import com.epms.model.User;
import com.epms.service.AuthService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
/** Handles session-based login. */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            User user = authService.authenticate(req.getParameter("email"), req.getParameter("password"));
            HttpSession session = req.getSession(true); session.setMaxInactiveInterval(1800); session.setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (AuthenticationException | DAOException e) {
            req.setAttribute("error", e.getMessage()); doGet(req, res);
        }
    }
}
