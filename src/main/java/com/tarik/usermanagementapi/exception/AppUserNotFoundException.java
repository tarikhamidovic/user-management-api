package com.tarik.usermanagementapi.exception;

import lombok.Data;

@Data
public class AppUserNotFoundException extends RuntimeException{

    private final String message;
    public AppUserNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
