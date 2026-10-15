package com.epms.dao;

import com.epms.exception.DAOException;
import com.epms.model.*;
import com.epms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation for user authentication and administration. */
public class JdbcUserDAO implements GenericDAO<User, Integer>, UserDAO {
    @Override public User findByEmail(String email) throws DAOException {
        String sql = "SELECT id,name,email,password_hash,role,department_id FROM users WHERE email=? AND active=true";
        try (Connection c = DBConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) { return r.next() ? map(r) : null; }
        } catch (SQLException e) { throw new DAOException("Unable to find user", e); }
    }
    @Override public User save(User user) throws DAOException { throw new UnsupportedOperationException("Use a role-specific user service"); }
    @Override public User findById(Integer id) throws DAOException { throw new UnsupportedOperationException("Not implemented"); }
    @Override public List<User> findAll() throws DAOException { return new ArrayList<>(); }
    @Override public void update(User user) throws DAOException { throw new UnsupportedOperationException("Not implemented"); }
    @Override public void delete(Integer id) throws DAOException { throw new UnsupportedOperationException("Not implemented"); }
    private User map(ResultSet r) throws SQLException {
        int id = r.getInt("id"); String role = r.getString("role");
        return switch (role) {
            case "ADMIN" -> new Admin(id, r.getString("name"), r.getString("email"), r.getString("password_hash"), (Integer) r.getObject("department_id"));
            case "MANAGER" -> new Manager(id, r.getString("name"), r.getString("email"), r.getString("password_hash"), (Integer) r.getObject("department_id"));
            default -> new Employee(id, r.getString("name"), r.getString("email"), r.getString("password_hash"), (Integer) r.getObject("department_id"));
        };
    }
}
