package com.spring.todo.service;

import com.spring.todo.entity.User;
import com.spring.todo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    TodoService todoService;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<String> findAllUserNames() {
        return userRepo.findAllUsernames();
    }

    public User findByName(String username) {
        return userRepo.findByUsername(username);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public void deleteByName(String username) {
        if (todoService.findByUsername(username) != null)
            throw new RuntimeException("Can't delete. User is associated with a todo");

        findByName(username).setAuthorities(null);
        userRepo.deleteByUsername(username);
    }
}