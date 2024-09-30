package com.spring.todo.service;

import com.spring.todo.entity.Todo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

//@Service
public class UnusedTodoService {

    private static final List<Todo> todos = new ArrayList<Todo>();
    static int item = 0;

    static {
        todos.add(new Todo(++item, "Supriyo", "Learn SpringBoot", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++item, "Supriyo", "Learn DevOps", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++item, "Supriyo", "Learn Full Stack", LocalDate.now().plusYears(1), false));
    }

    public List<Todo> findByUsername(String Username) {
        Predicate<Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(Username);
        return todos.stream().filter(predicate).toList();
    }

    public void addATodo(String username, String description, LocalDate targetDate, boolean done) {
        todos.add(new Todo(++item, username, description, targetDate, done));
    }

    public void deleteById(int id) {
        Predicate<Todo> predicate = todo -> todo.getId() == id;
        todos.removeIf(predicate);
    }

    public Todo findById(int id) {
        Predicate<Todo> predicate = todo -> todo.getId() == id;
        return todos.stream().filter(predicate).findFirst().get();
    }

    public void updateTodo(Todo todo) {
        deleteById(todo.getId());
        todos.add(todo);
    }
}