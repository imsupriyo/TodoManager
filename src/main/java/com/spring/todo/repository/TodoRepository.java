package com.spring.todo.repository;

import com.spring.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUsernameAndDoneOrderByTargetDate(String username, boolean done);

    List<Todo> findByDoneOrderByTargetDate(boolean done);

    @Modifying
    @Query(value = "UPDATE Todo t SET t.done=true WHERE t.id= :id")
    void markTodoAsDone(@Param("id") int id);

}