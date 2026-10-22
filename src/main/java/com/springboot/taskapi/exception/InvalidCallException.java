package com.springboot.taskapi.exception;

public class InvalidCallException extends RuntimeException {
    public InvalidCallException(String message) {
        super(message);
    }
}
