package com.spring.todo.advice;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException() {
    }

    public TodoNotFoundException(String message) {
        super(message);
    }

    public TodoNotFoundException(Throwable cause) {
        super(cause);
    }
}