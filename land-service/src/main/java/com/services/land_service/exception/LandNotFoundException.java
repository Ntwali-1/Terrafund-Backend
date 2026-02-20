package com.services.land_service.exception;

public class LandNotFoundException extends RuntimeException {
    public LandNotFoundException(String message) {
        super(message);
    }
}
