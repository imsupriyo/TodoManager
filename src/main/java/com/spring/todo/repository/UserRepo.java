package com.spring.todo.repository;

import com.spring.todo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {

    @Query(value = "SELECT username FROM users", nativeQuery = true)
    List<String> findAllUsernames();

    Optional<User> findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "Delete FROM users WHERE username = :username", nativeQuery = true)
    void deleteByUsername(@Param("username") String username);

}