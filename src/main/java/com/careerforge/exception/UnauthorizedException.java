// src/main/java/com/careerforge/exception/UnauthorizedException.java
package com.careerforge.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
