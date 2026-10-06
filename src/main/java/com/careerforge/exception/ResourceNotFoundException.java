// src/main/java/com/careerforge/exception/ResourceNotFoundException.java
package com.careerforge.exception;

// OOP INHERITANCE: Extends RuntimeException, inherits message/cause chain
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
