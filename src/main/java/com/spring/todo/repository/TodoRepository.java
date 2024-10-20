package com.spring.todo.repository;

import com.spring.todo.entity.Todo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUsernameAndDoneOrderByTargetDate(String username, boolean done);

    List<Todo> findByDoneOrderByTargetDate(boolean done);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Todo t SET t.done=true WHERE t.id= :id")
    void markTodoAsDone(@Param("id") int id);

}