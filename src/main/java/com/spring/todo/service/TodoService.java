package com.spring.todo.service;

import com.spring.todo.repository.TodoRepository;
import com.spring.todo.entity.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public List<Todo> findByUsername(String username) {
        List<Todo> todoList = todoRepository.findByUsername(username);
        System.out.println(todoList);
        return todoList.isEmpty() ? null : todoList;
    }

    public void deleteById(int id) {
        todoRepository.deleteById(id);
    }

    public void save(Todo todo) {
        todoRepository.save(todo);
    }

    public Todo findById(int id) {
        return todoRepository.findById(id).orElse(null);
    }
}
