package com.spring.todo.controller;

import com.spring.todo.entity.Todo;
import com.spring.todo.service.UnusedTodoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

// Without JPA support - own business logic
//@Controller
@SessionAttributes("name")
public class Controller {

    private final UnusedTodoService todoService;

    public Controller(UnusedTodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping("todo")
    public String gotoTodos(ModelMap model) {
        List<Todo> todos = todoService.findByUsername(getLoggedUsername());
        model.addAttribute("todos", todos);
        return "todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String defaultTodo(ModelMap model) {
        Todo todo = new Todo(0, getLoggedUsername(), "", LocalDate.now().plusYears(1), false);
        model.put("todo", todo);
        return "add-todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addTodo(ModelMap model, Todo todo) {
        todoService.addATodo(getLoggedUsername(), todo.getDescription(), todo.getTargetDate(), false);
        return "redirect:todo";
    }

    @RequestMapping(value = "delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam int id) {
        todoService.deleteById(id);
        return "redirect:todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String goToUpdateTodo(@RequestParam int id, ModelMap model) {
        Todo todo = todoService.findById(id);
        model.addAttribute("todo", todo);
        return "add-todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateTodo(ModelMap model, Todo todo) {
        todoService.updateTodo(todo);
        return "redirect:todo";
    }

    private String getLoggedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}