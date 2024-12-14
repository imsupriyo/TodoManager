package com.spring.todo.controller;

import com.spring.todo.advice.UserNotFoundException;
import com.spring.todo.service.TodoService;
import com.spring.todo.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("classpath:sql.properties")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.scripts.create.todo}")
    private String createTodo;

    @Value("${sql.scripts.delete.todo}")
    private String deleteTodo;

    @BeforeEach
    public void insertData() {
        jdbcTemplate.execute(createTodo);
    }

    @AfterEach
    public void deleteData() {
        jdbcTemplate.execute(deleteTodo);
    }

    @DisplayName("add todo as admin")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void adminAddTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/todo/admin/add-todo")
                        .param("id", String.valueOf(2))
                        .param("username", "marry")
                        .param("description", "Learn Mockito")
                        .param("targetDate", LocalDate.now().toString())
                        .param("done", "false"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertNotNull(todoService.findTodoByUsername("marry"));
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/todo/admin/list-todo");
    }

    @DisplayName("admin-view getMapping")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAdminView() throws Exception {
        MvcResult result = mockMvc.perform(get("/todo/admin/list-todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "all-todos");
    }

    @DisplayName("admin-add getMapping")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAdminAdd() throws Exception {
        MvcResult result = mockMvc.perform(get("/todo/admin/add-todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "admin-add-todo");
    }

    @DisplayName("Add a User")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addNewUser() throws Exception {
        assertThrows(UserNotFoundException.class, () -> userService.findByName("deadpool"));
        mockMvc.perform(post("/todo/admin/add-user")
                        .param("username", "deadpool")
                        .param("password", "abc@123")
                        .param("roles", "ROLE_ADMIN")
                        .param("roles", "ROLE_EMPLOYEE"))
                .andExpect(redirectedUrl("/todo/admin/list-todo"))
                .andReturn();
        assertNotNull(userService.findByName("deadpool"));
    }

    @DisplayName("Add a User with existing username")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addUserWithExistingUsername() throws Exception {
        assertNotNull(userService.findByName("supriyo"));
        MvcResult result = mockMvc.perform(post("/todo/admin/add-user")
                        .param("username", "supriyo")
                        .param("password", "abc@123")
                        .param("roles", "ROLE_ADMIN")
                        .param("roles", "ROLE_EMPLOYEE"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndViewAssert.assertViewName(result.getModelAndView(), "error");
    }

    @DisplayName("delete a user")
    @Test
    @DirtiesContext
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void deleteUser() throws Exception {
        assertNotNull(userService.findByName("john"));
        mockMvc.perform(post("/todo/admin/del-user")
                        .param("username", "john"))
                .andExpect(redirectedUrl("/todo/admin/list-todo"))
                .andReturn();
        assertThrows(UserNotFoundException.class, () -> userService.findByName("john"));
    }

    @DisplayName("delete own account")
    @Test
    @DirtiesContext
    @WithMockUser(username = "marry", roles = {"ADMIN"})
    public void deleteOwnAccount() throws Exception {
        assertNotNull(userService.findByName("marry"));
        MvcResult result = mockMvc.perform(post("/todo/admin/del-user")
                        .param("username", "marry"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndViewAssert.assertViewName(result.getModelAndView(), "error");
    }

    @DisplayName("delete account connected with a todo")
    @Test
    @DirtiesContext
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void deleteAccountAssociatedWithATodo() throws Exception {
        assertNotNull(todoService.findTodoByUsername("supriyo"));
        MvcResult result = mockMvc.perform(post("/todo/admin/del-user")
                        .param("username", "supriyo"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndViewAssert.assertViewName(result.getModelAndView(), "error");
    }

    @DisplayName("select more than 2 user roles")
    @Test
    @DirtiesContext
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void moreThanTwoUserRoles() throws Exception {
        MvcResult result = mockMvc.perform(post("/todo/admin/add-user")
                        .param("username", "deadpool")
                        .param("password", "test@123")
                        .param("roles", "ROLE_ADMIN")
                        .param("roles", "ROLE_MANAGER")
                        .param("roles", "ROLE_EMPLOYEE"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndViewAssert.assertViewName(result.getModelAndView(), "userform");
    }
}
