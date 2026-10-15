package com.epms.service;

import com.epms.exception.DAOException;
import com.epms.exception.InvalidRatingException;
import com.epms.model.Goal;
import com.epms.model.Review;
import com.epms.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * JDBC-backed application operations used by the portal controllers.
 */
public class PortalService {
    /** Loads employees, optionally filtered by name. */
    public List<Map<String, Object>> employees(String search) throws DAOException {
        String sql = "SELECT u.id,u.name,u.email,u.role,u.department_id,d.name department,m.name manager " +
                "FROM users u LEFT JOIN departments d ON d.id=u.department_id LEFT JOIN users m ON m.id=u.manager_id " +
                "WHERE u.role='EMPLOYEE' AND lower(u.name) LIKE lower(?) ORDER BY u.name";
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, "%" + (search == null ? "" : search.trim()) + "%");
            try (ResultSet r = p.executeQuery()) { while (r.next()) rows.add(row(r)); }
            return rows;
        } catch (SQLException e) { throw new DAOException("Unable to load employees", e); }
    }

    /** Loads only employees assigned to a manager for safe team assignment. */
    public List<Map<String, Object>> teamEmployees(int managerId) throws DAOException {
        return query("SELECT id,name,email FROM users WHERE role='EMPLOYEE' AND manager_id=? AND active=true ORDER BY name", managerId);
    }

    /** Loads all departments. */
    public List<Map<String, Object>> departments() throws DAOException {
        return query("SELECT d.id,d.name,COUNT(u.id) employee_count " +
                "FROM departments d LEFT JOIN users u ON u.department_id=d.id AND u.role='EMPLOYEE' " +
                "GROUP BY d.id,d.name ORDER BY d.name");
    }

    /** Loads active managers for assignment selectors. */
    public List<Map<String, Object>> managers() throws DAOException {
        return query("SELECT id,name,email FROM users WHERE role='MANAGER' AND active=true ORDER BY name");
    }

    /** Loads open cycles for review and goal forms. */
    public List<Map<String, Object>> openCycles() throws DAOException {
        return query("SELECT id,name,start_date,end_date FROM review_cycles WHERE status='OPEN' ORDER BY start_date DESC");
    }

    /** Loads review cycles. */
    public List<Map<String, Object>> cycles() throws DAOException {
        return query("SELECT id,name,start_date,end_date,status FROM review_cycles ORDER BY start_date DESC");
    }

    /** Loads goals for an employee or manager's team. */
    public List<Map<String, Object>> goals(int userId, boolean manager) throws DAOException {
        String sql = manager
                ? "SELECT g.*,u.name employee_name FROM goals g JOIN users u ON u.id=g.employee_id WHERE g.manager_id=? ORDER BY g.due_date"
                : "SELECT g.*,u.name employee_name FROM goals g JOIN users u ON u.id=g.employee_id WHERE g.employee_id=? ORDER BY g.due_date";
        return query(sql, userId);
    }

    /** Loads reviews for an employee or manager's team. */
    public List<Map<String, Object>> reviews(int userId, boolean manager) throws DAOException {
        String sql = manager
                ? "SELECT r.*,u.name employee_name,c.name cycle_name FROM reviews r JOIN users u ON u.id=r.employee_id JOIN review_cycles c ON c.id=r.cycle_id WHERE r.reviewer_id=? ORDER BY r.created_at DESC"
                : "SELECT r.*,u.name reviewer_name,c.name cycle_name FROM reviews r JOIN users u ON u.id=r.reviewer_id JOIN review_cycles c ON c.id=r.cycle_id WHERE r.employee_id=? ORDER BY r.created_at DESC";
        return query(sql, userId);
    }

    /** Loads notifications for a user. */
    public List<Map<String, Object>> notifications(int userId) throws DAOException {
        return query("SELECT id,message,is_read,created_at FROM notifications WHERE user_id=? ORDER BY created_at DESC", userId);
    }

    /** Creates an employee using a BCrypt password hash supplied by the caller. */
    public void createEmployee(String name, String email, String passwordHash, int departmentId, int managerId)
            throws DAOException {
        if (!exists("SELECT 1 FROM departments WHERE id=?", departmentId)) {
            throw new DAOException("The selected department does not exist.", null);
        }
        if (!exists("SELECT 1 FROM users WHERE id=? AND role='MANAGER' AND active=true", managerId)) {
            throw new DAOException("The selected manager is not valid.", null);
        }
        execute("INSERT INTO users(name,email,password_hash,role,department_id,manager_id) VALUES(?,?,?,'EMPLOYEE',?,?)",
                name, email, passwordHash, departmentId, managerId);
    }

    /** Creates a department. */
    public void createDepartment(String name) throws DAOException {
        execute("INSERT INTO departments(name) VALUES(?) ON CONFLICT (name) DO NOTHING", name);
    }

    /** Creates a review cycle. */
    public void createCycle(String name, LocalDate start, LocalDate end) throws DAOException {
        execute("INSERT INTO review_cycles(name,start_date,end_date,status) VALUES(?,?,?,'OPEN')", name, start, end);
    }

    /** Closes an existing review cycle. */
    public void closeCycle(int id) throws DAOException { execute("UPDATE review_cycles SET status='CLOSED' WHERE id=?", id); }

    /** Creates a goal for a manager's employee. */
    public void createGoal(Goal goal) throws DAOException {
        if (!exists("SELECT 1 FROM users WHERE id=? AND role='EMPLOYEE' AND active=true", goal.getEmployeeId())) {
            throw new DAOException("The selected employee is not valid.", null);
        }
        if (!exists("SELECT 1 FROM users WHERE id=? AND role='EMPLOYEE' AND manager_id=? AND active=true",
                goal.getEmployeeId(), goal.getManagerId())) {
            throw new DAOException("You can only assign goals to employees on your team.", null);
        }
        execute("INSERT INTO goals(employee_id,manager_id,title,description,weight,due_date,progress) VALUES(?,?,?,?,?,?,0)",
                goal.getEmployeeId(), goal.getManagerId(), goal.getTitle(), goal.getDescription(),
                goal.getWeight(), goal.getDueDate());
    }

    /** Updates employee goal progress. */
    public void updateProgress(int goalId, int employeeId, int progress) throws DAOException {
        execute("UPDATE goals SET progress=? WHERE id=? AND employee_id=?", progress, goalId, employeeId);
    }

    /** Persists a self-assessment or manager review. */
    public void saveReview(Review review, String notification) throws DAOException, InvalidRatingException {
        if (!exists("SELECT 1 FROM review_cycles WHERE id=? AND status='OPEN'", review.getCycleId())) {
            throw new DAOException("The selected review cycle is not open.", null);
        }
        if ("MANAGER".equals(review.getType()) && !exists(
                "SELECT 1 FROM users WHERE id=? AND role='EMPLOYEE' AND manager_id=? AND active=true",
                review.getEmployeeId(), review.getReviewerId())) {
            throw new DAOException("You can only review employees on your team.", null);
        }
        if ("SELF".equals(review.getType()) && review.getEmployeeId() != review.getReviewerId()) {
            throw new DAOException("A self-assessment must belong to the signed-in employee.", null);
        }
        new ReviewService().submitReview(review, notification);
    }

    /** Builds report metrics for the report page. */
    public Map<String, Object> report() throws DAOException {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("departmentAverages", query("SELECT d.name,COALESCE(ROUND(AVG(r.rating)::numeric,2),0) average " +
                "FROM departments d LEFT JOIN users u ON u.department_id=d.id LEFT JOIN reviews r ON r.employee_id=u.id " +
                "GROUP BY d.name ORDER BY d.name"));
        result.put("completion", query("SELECT d.name,COALESCE(ROUND(AVG(g.progress)::numeric,2),0) completion " +
                "FROM departments d LEFT JOIN users u ON u.department_id=d.id LEFT JOIN goals g ON g.employee_id=u.id " +
                "GROUP BY d.name ORDER BY d.name"));
        result.put("distribution", query("SELECT rating,COUNT(*) count FROM reviews GROUP BY rating ORDER BY rating"));
        result.put("topPerformers", query("SELECT u.name,ROUND(AVG(r.rating)::numeric,2) average FROM users u " +
                "JOIN reviews r ON r.employee_id=u.id GROUP BY u.name ORDER BY average DESC LIMIT 5"));
        return result;
    }

    private List<Map<String, Object>> query(String sql, Object... params) throws DAOException {
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, params);
            try (ResultSet r = p.executeQuery()) { while (r.next()) rows.add(row(r)); }
            return rows;
        } catch (SQLException e) { throw new DAOException("Unable to load portal data", e); }
    }

    private void execute(String sql, Object... params) throws DAOException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, params); p.executeUpdate();
        } catch (SQLException e) { throw new DAOException("Unable to save portal data", e); }
    }

    private boolean exists(String sql, Object... params) throws DAOException {
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bind(p, params);
            try (ResultSet r = p.executeQuery()) { return r.next(); }
        } catch (SQLException e) {
            throw new DAOException("Unable to validate assignment", e);
        }
    }

    private static void bind(PreparedStatement p, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) p.setObject(i + 1, params[i]);
    }

    private static Map<String, Object> row(ResultSet r) throws SQLException {
        Map<String, Object> row = new LinkedHashMap<>();
        ResultSetMetaData meta = r.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) row.put(meta.getColumnLabel(i), r.getObject(i));
        return row;
    }
}
