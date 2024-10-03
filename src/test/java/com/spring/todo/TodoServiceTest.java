package com.spring.todo;

import com.spring.todo.entity.Todo;
import com.spring.todo.service.TodoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

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

    //    @Disabled("Don't run until JiRA: xxx is fixed")
    @DisplayName("Id Not Null")
    @Test
    public void idNotNull() {
        Todo todo = todoService.findById(101);
        assertNotNull(todo, "todo shouldn't be Null");
        assertEquals("todo{id='101', username='supriyo', description='Learn AWS', targetDate=" + LocalDate.now() + ", done=false}",
                todo.toString());
    }

    //    @EnabledOnOs(OS.WINDOWS)
    @DisplayName("Invalid Id")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    public void invalidId(int id) {
        assertNull(todoService.findById(id));
    }

    @DisplayName("Username Validation")
    @Test
    public void userName() {
        assertEquals("supriyo", todoService.findById(101).getUsername());
    }

    @DisplayName("Delete By Id")
    @Test
    public void deleteById() {
        assertNotNull(todoService.findById(101));
        todoService.deleteById(101);
        assertNull(todoService.findById(0));
    }

    @DisplayName("Save entity")
    @Test
    public void saveEntity() {
        Todo todo = new Todo(
                1,
                "supriyo",
                "Learn JUnit",
                LocalDate.now(),
                false
        );
        todoService.save(todo);
        Todo temp = todoService.findById(1);
        assertNotNull(temp);
        assertEquals(todo.toString(), temp.toString());
    }

    @DisplayName("Find By Username")
    @Test
    public void findByUserName() {
        assertNotNull(todoService.findTodoByUsername("supriyo"));
    }
}