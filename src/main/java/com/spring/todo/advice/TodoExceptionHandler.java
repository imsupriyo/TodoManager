package com.spring.todo.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class TodoExceptionHandler {

    @ExceptionHandler(value = TodoNotFoundException.class)
    ResponseEntity<TodoErrorResponse> invalidIdError(TodoNotFoundException ex) {
        TodoErrorResponse response = new TodoErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDate.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    ResponseEntity<TodoErrorResponse> genericException(Exception ex) {
        TodoErrorResponse response = new TodoErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDate.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}