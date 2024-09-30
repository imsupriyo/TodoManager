package com.spring.todo.repository;

import com.spring.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

    // Using Custom query to fetch Todos in asc order
    @Query(value = "SELECT * FROM todo WHERE username= :username ORDER BY target_date", nativeQuery = true)
    public List<Todo> findByUsername(String username);

    // Using Custom query to fetch Todos in asc order
    @Query(value = "SELECT * FROM todo ORDER BY target_date ASC", nativeQuery = true)
    public List<Todo> findAll();
}