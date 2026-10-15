package com.epms.controller;
import com.epms.exception.DAOException;
import com.epms.service.PortalService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.model.User;
import com.epms.util.PasswordUtil;
/** Entry point for administrator employee CRUD operations. */
@WebServlet("/admin/employees")
public class EmployeeServlet extends HttpServlet {
    private final PortalService service = new PortalService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            req.setAttribute("employees", service.employees(req.getParameter("search")));
            req.setAttribute("departments", service.departments());
            req.setAttribute("managers", service.managers());
            req.getRequestDispatcher("/WEB-INF/views/admin/employees.jsp").forward(req, res);
        } catch (DAOException e) { throw new ServletException("Unable to load employees", e); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            service.createEmployee(req.getParameter("name"), req.getParameter("email"),
                    PasswordUtil.hash(req.getParameter("password")),
                    Integer.parseInt(req.getParameter("departmentId")),
                    Integer.parseInt(req.getParameter("managerId")));
            res.sendRedirect(req.getContextPath()+"/admin/employees");
        } catch (Exception e) { throw new IOException("Unable to create employee", e); }
    }
}
