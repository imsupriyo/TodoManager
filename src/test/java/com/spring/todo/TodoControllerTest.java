package com.spring.todo;

import com.spring.todo.entity.User;
import com.spring.todo.service.TodoService;
import com.spring.todo.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        MvcResult result = mockMvc.perform(get("/todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "todo");
    }

    @DisplayName("Access denied page")
    @Test
    @WithMockUser(username = "john", roles = {"EMPLOYEE"})
    public void accessDeniedPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access-denied"))
                .andReturn();

        MvcResult accessDeniedResult = mockMvc.perform(get("/access-denied"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = accessDeniedResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "access-denied");
    }

    @DisplayName("Add todo button")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAddTodo() throws Exception {
        MvcResult result = mockMvc.perform(get("/add-todo"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "add-todo");
    }

    @DisplayName("Add new Todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/add-todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", request.getParameterValues("id"))
                        .param("username", request.getParameterValues("username"))
                        .param("description", request.getParameterValues("description"))
                        .param("targetDate", request.getParameterValues("target_date"))
                        .param("done", request.getParameterValues("done")))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:todo");
        assertNotNull(todoService.findById(todoService.findAll().size()));
    }

    @DisplayName("Past target date")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void addInvalidTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/add-todo")
                        .contentType(MediaType.APPLICATION_JSON)
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
        MvcResult result = mockMvc.perform(get("/delete-todo?id=101"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:todo");
        assertNull(todoService.findById(101));
    }

    @DisplayName("Delete Invalid Todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void deleteInvalidTodo() throws Exception {
        assertNull(todoService.findById(404));
        MvcResult result = mockMvc.perform(get("/delete-todo?id=404"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Invalid Todo Id")))
                .andReturn();
    }

    @DisplayName("Admin add todo")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void adminAddTodo() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin-add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(2))
                        .param("username", "marry")
                        .param("description", "Learn Mockito")
                        .param("targetDate", LocalDate.now().toString())
                        .param("done", "false"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertNotNull(todoService.findByUsername("marry"));
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "redirect:admin");
    }
    @DisplayName("admin-view get Test")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAdminView() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "all-todos");
    }

    @DisplayName("admin-add get Test")
    @Test
    @WithMockUser(username = "supriyo", roles = {"ADMIN"})
    public void testAdminAdd() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin-add"))
                .andExpect(status().isOk())
                .andReturn();
        ModelAndView mav = result.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "admin-add-todo");
    }
}
