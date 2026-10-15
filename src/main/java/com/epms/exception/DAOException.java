package com.epms.exception;

/** Checked exception that translates JDBC failures at the DAO boundary. */
public class DAOException extends Exception {
    public DAOException(String message, Throwable cause) { super(message, cause); }
}
