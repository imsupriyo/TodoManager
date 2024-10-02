package com.spring.todo.controller;

import com.spring.todo.advice.TodoNotFoundException;
import com.spring.todo.entity.Todo;
import com.spring.todo.entity.UserRole;
import com.spring.todo.service.AuthoritiesService;
import com.spring.todo.service.TodoService;
import com.spring.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class AdminTodoController {
    private final TodoService todoService;
    private final UserService userService;
    private final AuthoritiesService authoritiesService;

    public AdminTodoController(TodoService todoService, UserService userService, AuthoritiesService authoritiesService) {
        this.todoService = todoService;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
    }

    // get username of the logged-in user :)
    private String getLoggedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/admin")
    public String admin(ModelMap model) {
        model.addAttribute("name", StringUtils.capitalize(getLoggedUsername()));
        model.addAttribute("todos", todoService.findAll());
        return "all-todos";
    }

    @GetMapping("/admin-add")
    public String adminAddTodo(ModelMap model) {
        model.put("todo", new Todo("", LocalDate.now(), false));
        model.put("users", userService.findAllUserNames());
        return "admin-add-todo";
    }

    @PostMapping("/admin-add")
    public String adminAddTodo(ModelMap model, @ModelAttribute("todo") @Valid Todo todo, BindingResult result) {
        // return the same page if validation fails
        if (result.hasErrors()) {
            // since returning the form, so fill the model again
            model.put("users", userService.findAllUserNames());
            return "admin-add-todo";
        }
        todoService.save(todo);
        return "redirect:admin"; // this avoids form re-submission caused by URL refresh
    }

    @RequestMapping(value = "update-admin-todo", method = RequestMethod.GET)
    public String adminUpdateTodo(@RequestParam("id") int id, ModelMap model) {
        if (todoService.findById(id) == null)
            throw new TodoNotFoundException("Invalid Todo Id");
        model.addAttribute("todo", todoService.findById(id));
        model.put("users", userService.findAllUserNames());
        return "admin-add-todo";
    }

    @RequestMapping(value = "update-admin-todo", method = RequestMethod.POST)
    public String adminUpdateTodo(@Valid Todo todo, ModelMap model, BindingResult result) {
        // return to the same page if validation fails
        if (result.hasErrors()) {
            model.put("users", userService.findAllUserNames());
            model.addAttribute("todo", todo);
            return "admin-add-todo";
        }
        todoService.save(todo);
        return "redirect:/admin";
    }

    @RequestMapping(value = "delete-admin-todo", method = RequestMethod.GET)
    public String adminDeleteTodo(@RequestParam("id") int id) {
        todoService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/register")
    public String addNewUser(Model model) {
        model.addAttribute("userForm", new UserRole("", ""));
        model.addAttribute("roleOptions", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        return "userform";
    }

    @PostMapping("/register")
    public String addNewUser(@ModelAttribute("userRole") UserRole userRole) {
        authoritiesService.addUserRole(userRole);
        return "redirect:/admin";
    }

    @GetMapping("/del-user")
    public String deleteUser(Model model) {
        model.addAttribute("users", userService.findAllUserNames());
        return "delete-user";
    }

    @PostMapping("/del-user")
    public String deletedUser(@RequestParam("username") String username) {
        if (getLoggedUsername().equals(username))
            throw new RuntimeException("You can't delete your own account!");

        userService.deleteByName(username);
        return "redirect:/admin";
    }
}