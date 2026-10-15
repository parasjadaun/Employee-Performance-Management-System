package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.model.*;
import com.epms.service.PortalService;
import com.epms.exception.*;
/** Entry point for manager reviews and employee self-assessments. */
@WebServlet({"/team/reviews", "/employee/self-assessment"})
public class ReviewServlet extends HttpServlet {
    private final PortalService service = new PortalService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        User u=(User)req.getSession().getAttribute("user");
        try {
            boolean manager = "MANAGER".equals(u.getRole());
            req.setAttribute("reviews", service.reviews(u.getId(), manager));
            req.setAttribute("cycles", service.openCycles());
            if (manager) req.setAttribute("employees", service.teamEmployees(u.getId()));
            req.getRequestDispatcher("/WEB-INF/views/reviews.jsp").forward(req,res);
        }
        catch (DAOException e) { throw new ServletException("Unable to load reviews",e); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User u=(User)req.getSession().getAttribute("user");
        try {
            Review r=new Review(Integer.parseInt(req.getParameter("cycleId")),Integer.parseInt(req.getParameter("employeeId")),u.getId(),Integer.parseInt(req.getParameter("rating")),req.getParameter("comments"),req.getParameter("strengths"),req.getParameter("improvements"),"MANAGER".equals(u.getRole())?"MANAGER":"SELF");
            service.saveReview(r,"A new performance review is available.");
            res.sendRedirect(req.getContextPath()+("/MANAGER".equals(u.getRole())?"/team/reviews":"/employee/self-assessment"));
        } catch (Exception e) { throw new IOException("Unable to save review",e); }
    }
}
