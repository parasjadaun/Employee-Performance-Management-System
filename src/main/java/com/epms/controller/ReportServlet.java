package com.epms.controller;
import com.epms.util.CsvExporter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;
import com.epms.service.PortalService;
import com.epms.exception.DAOException;
import javax.servlet.ServletException;
/** Serves the report page and a CSV export endpoint. */
@WebServlet(urlPatterns = {"/reports", "/reports.csv"})
public class ReportServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        if (req.getServletPath().endsWith(".csv")) {
            res.setContentType("text/csv"); res.setHeader("Content-Disposition", "attachment; filename=epms-report.csv");
            try {
                var report=new PortalService().report();
                var rows=new java.util.ArrayList<String[]>();
                rows.add(new String[]{"Department","Average Rating"});
                for (var row:(java.util.List<java.util.Map<String,Object>>)report.get("departmentAverages")) rows.add(new String[]{String.valueOf(row.get("name")),String.valueOf(row.get("average"))});
                CsvExporter.write(res.getWriter(), rows);
            } catch (DAOException e) { throw new IOException("Unable to export report",e); }
        } else {
            try { req.setAttribute("report",new PortalService().report()); req.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(req,res); }
            catch (DAOException e) { throw new ServletException("Unable to load report",e); }
        }
    }
}
