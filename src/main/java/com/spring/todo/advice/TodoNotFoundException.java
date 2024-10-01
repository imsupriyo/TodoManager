package com.spring.todo.advice;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException() {
    }

    public TodoNotFoundException(String message) {
        super(message);
    }
}