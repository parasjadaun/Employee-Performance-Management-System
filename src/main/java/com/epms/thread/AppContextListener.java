package com.epms.thread;
import com.epms.util.DBConnection;
import com.epms.util.SchemaInitializer;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
/** Initializes shared resources and shuts them down gracefully. */
@WebListener
public class AppContextListener implements ServletContextListener {
    private ReportScheduler scheduler;
    public void contextInitialized(ServletContextEvent event) {
        try {
            DBConnection.initialize();
            SchemaInitializer.initialize();
        } catch (Exception e) {
            throw new IllegalStateException("Application database initialization failed", e);
        }
        scheduler = new ReportScheduler(new SummaryCache()); scheduler.start();
    }
    public void contextDestroyed(ServletContextEvent event) {
        if (scheduler != null) scheduler.close();
        DBConnection.close();
    }
}
