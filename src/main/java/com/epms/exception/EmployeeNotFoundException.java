package com.epms.exception;
/** Raised when an employee lookup has no result. */
public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) { super(message); }
}
