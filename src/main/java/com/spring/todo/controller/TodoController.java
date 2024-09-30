package com.spring.todo.controller;

import com.spring.todo.advice.TodoNotFoundException;
import com.spring.todo.entity.Todo;
import com.spring.todo.entity.User;
import com.spring.todo.entity.UserRole;
import com.spring.todo.service.AuthoritiesService;
import com.spring.todo.service.TodoService;
import com.spring.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;
    private final AuthoritiesService authoritiesService;

    @Autowired
    private PasswordEncoder encoder;

    public TodoController(TodoService todoService, UserService userService, AuthoritiesService authoritiesService) {
        this.todoService = todoService;
        this.userService = userService;
        this.authoritiesService = authoritiesService;
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

    /**
     * Trim whitespaces filled in the form
     */
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
        List<Todo> todos = todoService.findByUsername(getLoggedUsername());
        model.addAttribute("name", StringUtils.capitalize(getLoggedUsername()));
        model.addAttribute("todos", todos);
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
        if (todoService.findById(id) == null)
            throw new TodoNotFoundException();
        todoService.deleteById(id);
        return "redirect:todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam("id") int id, ModelMap model) {
        Todo todo = todoService.findById(id);
        model.addAttribute("todo", todo);
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

    /**
     * Admin Access controllers
     */

    @GetMapping("/admin")
    public String admin(ModelMap model) {
        List<Todo> todos = todoService.findAll();
        model.addAttribute("name", StringUtils.capitalize(getLoggedUsername()));
        model.addAttribute("todos", todos);
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
            throw new TodoNotFoundException();
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
        if (todoService.findById(id) == null)
            throw new TodoNotFoundException();
        todoService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/register")
    public String addNewUser(Model model) {
        UserRole userRole = new UserRole("", "");
        model.addAttribute("userForm", userRole);
        model.addAttribute("roleOptions", List.of("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER"));
        return "userform";
    }

    @PostMapping("/register")
    public String addNewUser(@ModelAttribute("userRole") UserRole userRole) {
        if (userService.findAllUserNames().contains(userRole.getUsername()))
            throw new RuntimeException("Username is unavailable! try with a different name");
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
        if (todoService.findByUsername(username) != null)
            throw new RuntimeException("Can't delete. User is associated with a todo");

        if (getLoggedUsername().equals(username))
            throw  new RuntimeException("You can't delete your own account!");

        userService.deleteByName(username);
        return "redirect:/admin";
    }
}