package com.epms.controller;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import com.epms.model.User;
import com.epms.model.Goal;
import com.epms.service.PortalService;
import com.epms.exception.DAOException;
import java.time.LocalDate;
/** Entry point for manager goal assignment and employee progress updates. */
@WebServlet({"/team/goals", "/employee/goals"})
public class GoalServlet extends HttpServlet {
    private final PortalService service = new PortalService();
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        User user=(User)req.getSession().getAttribute("user");
        try {
            boolean manager = "MANAGER".equals(user.getRole());
            req.setAttribute("goals", service.goals(user.getId(), manager));
            if (manager) req.setAttribute("employees", service.teamEmployees(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/goals.jsp").forward(req,res);
        }
        catch (DAOException e) { throw new ServletException("Unable to load goals", e); }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user=(User)req.getSession().getAttribute("user");
        try {
            if ("progress".equals(req.getParameter("action"))) service.updateProgress(Integer.parseInt(req.getParameter("id")), user.getId(), Integer.parseInt(req.getParameter("progress")));
            else { Goal g=new Goal(); g.setEmployeeId(Integer.parseInt(req.getParameter("employeeId"))); g.setManagerId(user.getId()); g.setTitle(req.getParameter("title")); g.setDescription(req.getParameter("description")); g.setWeight(Double.parseDouble(req.getParameter("weight"))); g.setDueDate(LocalDate.parse(req.getParameter("dueDate"))); service.createGoal(g); }
            res.sendRedirect(req.getContextPath()+("/MANAGER".equals(user.getRole())?"/team/goals":"/employee/goals"));
        } catch (Exception e) { throw new IOException("Unable to save goal", e); }
    }
}
