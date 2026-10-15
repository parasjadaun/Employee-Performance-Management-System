package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
/** Invalidates the current login session. */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession(false); if (session != null) session.invalidate();
        res.sendRedirect(req.getContextPath() + "/login");
    }
}
