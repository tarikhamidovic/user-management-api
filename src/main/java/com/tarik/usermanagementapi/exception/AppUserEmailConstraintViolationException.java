package com.tarik.usermanagementapi.exception;

import lombok.Data;

@Data
public class AppUserEmailConstraintViolationException extends RuntimeException {

    private final String message;

    public AppUserEmailConstraintViolationException(String message) {
        super(message);
        this.message = message;
    }
}
