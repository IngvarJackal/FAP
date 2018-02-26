package ru.org.codingteam.fap.infrastructure.exceptions;

public class FapException extends RuntimeException {
    public FapException(String s) {
        super(s);
    }

    public FapException(Exception e) {
        super(e);
    }
}
