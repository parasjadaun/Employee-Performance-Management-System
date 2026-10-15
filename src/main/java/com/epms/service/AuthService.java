package com.epms.service;

import com.epms.dao.JdbcUserDAO;
import com.epms.exception.AuthenticationException;
import com.epms.exception.DAOException;
import com.epms.model.User;
import com.epms.util.PasswordUtil;

/** Authenticates users and returns the correct polymorphic User subtype. */
public class AuthService {
    private final JdbcUserDAO userDAO;
    public AuthService() { this(new JdbcUserDAO()); }
    public AuthService(JdbcUserDAO userDAO) { this.userDAO = userDAO; }
    /** Authenticates credentials without exposing password data to callers. */
    public User authenticate(String email, String password) throws AuthenticationException, DAOException {
        User user = userDAO.findByEmail(email);
        if (user == null || !PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }
        return user;
    }
}
