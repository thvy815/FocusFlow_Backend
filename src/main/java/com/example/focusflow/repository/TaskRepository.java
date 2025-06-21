package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.userId = :userId AND t.ctGroupId IS NULL")
    List<Task> findByUserId(Integer userId);

    List<Task> findByCtGroupIdIn(List<Integer> ctGroupId); 
    
    @Query("SELECT t FROM Task t JOIN CtGroupUser ct ON t.ctGroupId = ct.id WHERE ct.userId = :userId")
    List<Task> findGroupTasksByUserId(@Param("userId") Integer userId);
}
