package org.example.carsservice.Exceptions;

public class DuplicateCarException extends RuntimeException {
    public DuplicateCarException(String message) {
        super(message);
    }
}