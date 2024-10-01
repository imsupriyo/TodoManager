package com.spring.todo.service;

import com.spring.todo.entity.User;
import com.spring.todo.repository.AuthoritiesRepo;
import com.spring.todo.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final AuthoritiesRepo authoritiesRepo;

    public UserService(UserRepo userRepo, AuthoritiesRepo authoritiesRepo) {
        this.userRepo = userRepo;
        this.authoritiesRepo = authoritiesRepo;
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

    public void deleteByName(String name) {
        findByName(name).setAuthorities(null);
        userRepo.deleteByUsername(name);
    }
}