package com.spring.todo.advice;

import java.time.LocalDate;

public class TodoErrorResponse {
    private int status;
    private String message;
    private LocalDate time;

    public TodoErrorResponse(int status, String message, LocalDate time) {
        this.status = status;
        this.message = message;
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TodoErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
