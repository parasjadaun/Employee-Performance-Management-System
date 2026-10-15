package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.service.PortalService;
import com.epms.exception.DAOException;
import java.time.LocalDate;
/** Entry point for review-cycle administration. */
@WebServlet("/admin/cycles")
public class CycleServlet extends HttpServlet {
    private final PortalService service = new PortalService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try { req.setAttribute("cycles", service.cycles()); req.getRequestDispatcher("/WEB-INF/views/admin/cycles.jsp").forward(req,res); }
        catch (DAOException e) { throw new ServletException("Unable to load cycles", e); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            if ("close".equals(req.getParameter("action"))) service.closeCycle(Integer.parseInt(req.getParameter("id")));
            else service.createCycle(req.getParameter("name"), LocalDate.parse(req.getParameter("startDate")), LocalDate.parse(req.getParameter("endDate")));
            res.sendRedirect(req.getContextPath()+"/admin/cycles");
        } catch (Exception e) { throw new IOException("Unable to save review cycle", e); }
    }
}
