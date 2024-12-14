package com.spring.todo.service;

import com.spring.todo.advice.UserNotFoundException;
import com.spring.todo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @DisplayName("Valid User")
    @Test
    public void validUser() {
        List<String> allUserNames = userService.findAllUserNames();
        assertEquals("[supriyo, john, marry]", allUserNames.toString());
        assertEquals(3, allUserNames.size());
    }

    @DisplayName("Find By Name")
    @Sql("/insertData.sql")
    @Test
    public void findByName() {
        User user = userService.findByName("supriyo");
        assertNotNull(user);
        assertEquals("supriyo", user.getUsername());
        assertEquals("larry", userService.findByName("larry").getUsername());
        assertThrows(UserNotFoundException.class,
                () -> userService.findByName("test"), "it should return null");
    }
}
