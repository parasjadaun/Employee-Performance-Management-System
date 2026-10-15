package com.epms.exception;
/** Raised when a rating is outside the inclusive 1-5 range. */
public class InvalidRatingException extends Exception {
    public InvalidRatingException(String message) { super(message); }
}
