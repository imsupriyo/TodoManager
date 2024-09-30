package com.spring.todo.repository;

import com.spring.todo.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepo extends JpaRepository<Authorities, Integer> {
    Authorities findByAuthority(String authority);
}
