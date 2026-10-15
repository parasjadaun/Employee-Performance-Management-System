package com.epms.exception;
/** Raised when credentials cannot authenticate a user. */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) { super(message); }
}
