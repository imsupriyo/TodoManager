package com.spring.todo;

import com.spring.todo.entity.User;
import com.spring.todo.service.TodoService;
import com.spring.todo.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private static MockHttpServletRequest request;

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

    @BeforeAll
    public static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("id", String.valueOf(1));
        request.setParameter("username", "supriyo");
        request.setParameter("description", "Learn SpringBoot");
        request.setParameter("target_date", LocalDate.now().toString());
        request.setParameter("done", "false");
    }

    @DisplayName("todo list")
//    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testTodo() throws Exception {
        User supriyo = userService.findByName("supriyo");
        assertNotNull(supriyo);
        MvcResult result = mockMvc.perform(get("/todo/list-todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "todo");
    }

    @DisplayName("Access denied page")
    @Test
    @WithMockUser(username = "john", roles = {"EMPLOYEE"})
    public void accessDeniedPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/todo/admin/list-todo"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/todo/access-denied"))
                .andReturn();

        MvcResult accessDeniedResult = mockMvc.perform(get("/todo/access-denied"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = accessDeniedResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @DisplayName("Add todo button")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAddTodo() throws Exception {
        MvcResult result = mockMvc.perform(get("/todo/add-todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "add-todo");
    }

    @DisplayName("Add new Todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/todo/add-todo")
                        .param("id", request.getParameterValues("id"))
                        .param("username", request.getParameterValues("username"))
                        .param("description", request.getParameterValues("description"))
                        .param("targetDate", request.getParameterValues("target_date"))
                        .param("done", request.getParameterValues("done")))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/todo/list-todo");
        assertNotNull(todoService.findById(todoService.findAll().size()));
    }

    @DisplayName("Past target date")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addInvalidTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/todo/add-todo")
                        .param("id", String.valueOf(3))
                        .param("username", "supriyo")
                        .param("description", "Learn SQL")
                        .param("targetDate", LocalDate.now().minusYears(1).toString())
                        .param("done", "false"))
                .andExpect(status().isOk())
                .andReturn();
        assertNull(todoService.findById(3));
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "add-todo");
    }

    @DisplayName("Delete Todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void deleteTodo() throws Exception {
        assertNotNull(todoService.findById(101));
        MvcResult result = mockMvc.perform(get("/todo/delete-todo?id=101"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:/todo/list-todo");
        assertNull(todoService.findById(101));
    }

    @DisplayName("Delete Invalid Todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void deleteInvalidTodo() throws Exception {
        assertNull(todoService.findById(404));
        MvcResult result = mockMvc.perform(get("/todo/delete-todo?id=404"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndViewAssert.assertViewName(result.getModelAndView(), "error");
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
        assertNull(userService.findByName("deadpool"));
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
        assertNull(userService.findByName("john"));
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

    @DisplayName("mark Todo as Done")
    @Test
    @DirtiesContext
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void markTodoAsDone() throws Exception {
        assertFalse(todoService.findById(101).isDone());
        MvcResult result = mockMvc.perform(get("/todo/mark-done-todo?id=101"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertTrue(todoService.findById(101).isDone());
    }

    @DisplayName("re-mark Todo as Done")
    @Test
    @DirtiesContext
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void remarkTodoAsDone() throws Exception {
        assertFalse(todoService.findById(101).isDone());
        mockMvc.perform(get("/todo/mark-done-todo?id=101"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertTrue(todoService.findById(101).isDone());
        MvcResult result = mockMvc.perform(get("/todo/mark-done-todo?id=101"))
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
