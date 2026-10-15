package com.epms.dao;

import com.epms.exception.DAOException;
import java.util.List;

/** Generic CRUD contract shared by JDBC repositories. */
public interface GenericDAO<T, ID> {
    T save(T entity) throws DAOException;
    T findById(ID id) throws DAOException;
    List<T> findAll() throws DAOException;
    void update(T entity) throws DAOException;
    void delete(ID id) throws DAOException;
}
