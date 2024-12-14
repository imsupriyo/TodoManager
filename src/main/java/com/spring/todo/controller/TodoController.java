package com.spring.todo.controller;

import com.spring.todo.entity.Todo;
import com.spring.todo.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@SessionAttributes("name")
@RequestMapping("/todo")
public class TodoController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // get username of the logged-in user
    private String getLoggedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Trim whitespaces filled in the form
    @InitBinder
    private void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, editor);
    }

    @GetMapping("/access-denied")
    public void accessDenied(Model model) {
        throw new RuntimeException("Access Denied - Only Admins can access this page.");
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @RequestMapping(value = "/list-todo", method = RequestMethod.GET)
    public String incompleteTodos(ModelMap model) {
        model.addAttribute("name", StringUtils.capitalize(getLoggedUsername()));
        model.addAttribute("todos", todoService.findTodoByUsername(getLoggedUsername()));
        return "todo";
    }

    @GetMapping("/completed-todo")
    public String completedTodos(Model model) {
        model.addAttribute("todos", todoService.completedTodosByUsername(getLoggedUsername()));
        return "todo";
    }

    @GetMapping("/mark-done-todo")
    public String markTodoAsDone(@RequestParam("id") int id) {
        todoService.markTodoAsDone(id);
        return "redirect:/todo/list-todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String addTodo(ModelMap model) {
        model.put("todo", new Todo(getLoggedUsername(), "", LocalDate.now(), false));
        return "add-todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addTodo(@Valid Todo todo, BindingResult result) {
        // return to the same page if validation fails
        if (result.hasErrors()) {
            logger.error("Couldn't add Todo. Validation Failed. {}", result.getAllErrors());
            return "add-todo";
        }
        todoService.save(todo);
        return "redirect:/todo/list-todo";
    }

    @RequestMapping(value = "delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam("id") int id) {
        todoService.deleteById(id);
        return "redirect:/todo/list-todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam("id") int id, ModelMap model) {
        model.addAttribute("todo", todoService.findById(id));
        return "add-todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateTodo(@Valid Todo todo, BindingResult result) {
        // return to the same page if validation fails
        if (result.hasErrors()) {
            logger.error("Couldn't update Todo. Validation Failed. {}", result.getAllErrors());
            return "add-todo";
        }
        todoService.save(todo);
        return "redirect:/todo/list-todo";
    }

}