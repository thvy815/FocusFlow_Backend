package com.example.focusflow.repository;

import com.example.focusflow.entity.TaskAssignment;
import com.example.focusflow.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    List<TaskAssignment> findByTaskId(Integer taskId);
    void deleteByTaskId(Integer taskId);

    @Query("SELECT ta FROM TaskAssignment ta WHERE ta.ctGroupId = :groupId")
    List<TaskAssignment> findByGroupId(@Param("groupId") Integer groupId);
    
    @Query("SELECT u FROM User u " +
           "JOIN CtGroupUser c ON u.id = c.userId " +
           "JOIN TaskAssignment ta ON ta.ctGroupId = c.id " +
           "WHERE ta.taskId = :taskId")
    List<User> findUsersAssignedToTask(@Param("taskId") Integer taskId);
}
