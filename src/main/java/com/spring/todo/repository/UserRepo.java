package com.spring.todo.repository;

import com.spring.todo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    @Query(value = "SELECT username FROM users", nativeQuery = true)
    List<String> findAllUsernames();

    User findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}