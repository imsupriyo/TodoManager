package com.spring.todo.service;

import com.spring.todo.entity.Authorities;
import com.spring.todo.entity.User;
import com.spring.todo.entity.UserRole;
import com.spring.todo.repository.AuthoritiesRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthoritiesService {
    private final AuthoritiesRepo authoritiesRepo;
    private final UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    public AuthoritiesService(AuthoritiesRepo authoritiesRepo, UserService userService) {
        this.authoritiesRepo = authoritiesRepo;
        this.userService = userService;
    }

    @Transactional
    public void loadData() {
        Authorities admin = new Authorities("ROLE_ADMIN");
        Authorities employee = new Authorities("ROLE_EMPLOYEE");
        Authorities manager = new Authorities("ROLE_MANAGER");
        authoritiesRepo.saveAll(List.of(admin, employee, manager));

        User user1 = new User("supriyo", encoder.encode("abc@123"), (short) 1);
        user1.addAuthorities(findByAuthority("ROLE_ADMIN"));
        user1.addAuthorities(findByAuthority("ROLE_EMPLOYEE"));
        userService.save(user1);

        User user2 = new User("john", encoder.encode("abc@123"), (short) 1);
        user2.addAuthorities(findByAuthority("ROLE_EMPLOYEE"));
        userService.save(user2);

        User user3 = new User("marry", encoder.encode("abc@123"), (short) 1);
        user3.addAuthorities(findByAuthority("ROLE_EMPLOYEE"));
        user3.addAuthorities(findByAuthority("ROLE_MANAGER"));
        userService.save(user3);
    }

    public void save(Authorities authority) {
        authoritiesRepo.save(authority);
    }

    public Authorities findByAuthority(String authority) {
        return authoritiesRepo.findByAuthority(authority);
    }

    public void addUserRole(UserRole userRole) {
        if (userService.findAllUserNames().contains(userRole.getUsername()))
            throw new RuntimeException("Username is unavailable! try with a different name");

        User user = new User(userRole.getUsername(), encoder.encode(userRole.getPassword()), (short) 1);

        for (String role : userRole.getRoles()) {
            user.addAuthorities(findByAuthority(role));
        }
        userService.save(user);
    }

}
