package com.spring.todo.controller;

import com.spring.todo.entity.Todo;
import com.spring.todo.service.TodoService;
import jakarta.validation.Valid;
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
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // TODO: uncomment it once custom login page issue is fixed
//    @GetMapping("/login")
//    public String loginPage() {
//        return "basic_login";
//    }

    // get username of the logged-in user :)
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
    String accessDenied(Model model) {
        model.addAttribute("errorMessage",
                "Access Denied - Only Admins can access this page.");
        return "access-denied";
    }

    // redirect to Main app :P
    @GetMapping("/")
    public String redirectToDo() {
        return "redirect:/todo";
    }

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String gotoTodos(ModelMap model) {
        model.addAttribute("name", StringUtils.capitalize(getLoggedUsername()));
        model.addAttribute("todos", todoService.findByUsername(getLoggedUsername()));
        return "todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String addTodo(ModelMap model) {
        Todo todo = new Todo(0, getLoggedUsername(), "", LocalDate.now(), false);
        model.put("todo", todo);
        return "add-todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addTodo(@Valid Todo todo, BindingResult result) {
        // return to the same page if validation fails
        if (result.hasErrors()) {
            return "add-todo";
        }
        todoService.save(todo);
        return "redirect:todo";
    }

    @RequestMapping(value = "delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam("id") int id) {
        todoService.deleteById(id);
        return "redirect:todo";
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
            return "add-todo";
        }
        todoService.save(todo);
        return "redirect:todo";
    }

}