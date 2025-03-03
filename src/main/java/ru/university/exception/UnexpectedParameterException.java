package ru.university.exception;

public class UnexpectedParameterException extends RuntimeException {
    public UnexpectedParameterException(String paramName) {
        super("Unexpected param: " + paramName);
    }
}
