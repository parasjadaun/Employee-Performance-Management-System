package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.service.PortalService;
import com.epms.exception.DAOException;
/** Entry point for department management. */
@WebServlet("/admin/departments")
public class DepartmentServlet extends HttpServlet {
    private final PortalService service = new PortalService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try { req.setAttribute("departments", service.departments()); req.getRequestDispatcher("/WEB-INF/views/admin/departments.jsp").forward(req,res); }
        catch (DAOException e) { throw new ServletException("Unable to load departments", e); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try { service.createDepartment(req.getParameter("name")); res.sendRedirect(req.getContextPath()+"/admin/departments"); }
        catch (DAOException e) { throw new IOException("Unable to create department", e); }
    }
}
