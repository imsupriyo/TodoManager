package com.spring;

import com.spring.todo.service.AuthoritiesService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApp {

    @Autowired
    private AuthoritiesService authoritiesService;

    public static void main(String[] args) {
        SpringApplication.run(TodoApp.class, args);
    }

    @PostConstruct
    void init() {
        authoritiesService.loadData();
    }

}