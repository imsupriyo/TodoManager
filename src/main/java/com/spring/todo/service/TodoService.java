package com.spring.todo.service;

import com.spring.todo.advice.TodoNotFoundException;
import com.spring.todo.entity.Todo;
import com.spring.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findByDoneOrderByTargetDate(false);
    }

    public List<Todo> findTodoByUsername(String username) {
        List<Todo> todoList = todoRepository.findByUsernameAndDoneOrderByTargetDate(username, false);
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

    public List<Todo> completedTodosByUsername(String username) {
        return todoRepository.findByUsernameAndDoneOrderByTargetDate(username, true);
    }

    public List<Todo> allCompletedTodos() {
        return todoRepository.findByDoneOrderByTargetDate(true);
    }

    public void markTodoAsDone(int id) {
        if (findById(id).isDone())
            throw new RuntimeException("This Todo is already marked as completed.");
        todoRepository.markTodoAsDone(id);
    }
}
