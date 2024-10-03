package com.spring.todo.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TodoExceptionHandler {

    //    @ExceptionHandler(value = TodoNotFoundException.class)
//    ResponseEntity<TodoErrorResponse> invalidIdError(TodoNotFoundException ex) {
//        TodoErrorResponse response = new TodoErrorResponse(
//                HttpStatus.NOT_FOUND.value(),
//                ex.getMessage(),
//                LocalDate.now());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler
//    ResponseEntity<TodoErrorResponse> genericException(Exception ex) {
//        TodoErrorResponse response = new TodoErrorResponse(
//                HttpStatus.BAD_REQUEST.value(),
//                ex.getMessage(),
//                LocalDate.now());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(value = TodoNotFoundException.class)
    String invalidIdError(TodoNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(value = Exception.class)
    String genericException(Exception ex, Model model) {
        System.out.println("------------");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}