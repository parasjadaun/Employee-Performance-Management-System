package com.epms.dao;
import com.epms.exception.DAOException;
import com.epms.model.User;
/** User persistence operations. */
public interface UserDAO { User findByEmail(String email) throws DAOException; }
