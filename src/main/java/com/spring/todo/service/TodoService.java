package com.spring.todo.service;

import com.spring.todo.advice.TodoNotFoundException;
import com.spring.todo.entity.Todo;
import com.spring.todo.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "targetDate");
        return todoRepository.findAll(sort);
    }

    public List<Todo> findByUsername(String username) {
        List<Todo> todoList = todoRepository.findByUsernameOrderByTargetDate(username);
        return todoList.isEmpty() ? null : todoList;
    }

    public void deleteById(int id) {
        if (findById(id) == null)
            throw new TodoNotFoundException("Invalid Todo Id");
        todoRepository.deleteById(id);
    }

    public void save(Todo todo) {
        todoRepository.save(todo);
    }

    public Todo findById(int id) {
        return todoRepository.findById(id).orElse(null);
    }
}
