package com.epms.controller;
import com.epms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
/** Forwards users to the dashboard selected by runtime polymorphism. */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        req.getRequestDispatcher(user.getDashboardPath()).forward(req, res);
    }
}
