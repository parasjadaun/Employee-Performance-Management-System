package com.epms.exception;
/** Raised when submitted business data is invalid. */
public class ValidationException extends Exception {
    public ValidationException(String message) { super(message); }
}
