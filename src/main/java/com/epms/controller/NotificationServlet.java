package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.model.User;
import com.epms.service.PortalService;
import com.epms.exception.DAOException;
/** Displays notifications for the authenticated user. */
@WebServlet("/notifications")
public class NotificationServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try { User u=(User)req.getSession().getAttribute("user"); req.setAttribute("notifications",new PortalService().notifications(u.getId())); req.getRequestDispatcher("/WEB-INF/views/notifications.jsp").forward(req,res); }
        catch (DAOException e) { throw new ServletException("Unable to load notifications",e); }
    }
}
