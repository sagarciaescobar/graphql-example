package com.grahql.example.exception;

public class ProblemzAuthenticationException extends RuntimeException {
    public ProblemzAuthenticationException() {
        super("Invalid credential");
    }
}