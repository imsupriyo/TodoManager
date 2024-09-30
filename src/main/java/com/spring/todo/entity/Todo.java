package com.spring.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private int id;

    @NotNull(message = "username can't be empty")
    @Column(name = "username")
    private String username;

    @NotNull(message = "Description can't be empty")
    @Size(min = 5, message = "Description can't be shorter than 5 chars")
    @Size(max = 50, message = "Description can't be longer than 50 chars")
    @Column(name = "description")
    private String description;

    @FutureOrPresent(message = "Target date can't be in past")
    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "done")
    boolean done;

    public Todo() {
    }

    public Todo(int id, String username, String description, LocalDate targetDate, boolean done) {
        this.username = username;
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
    }

    public Todo(String description, LocalDate targetDate, boolean done) {
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "todo{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", targetDate=" + targetDate +
                ", done=" + done +
                '}';
    }
}