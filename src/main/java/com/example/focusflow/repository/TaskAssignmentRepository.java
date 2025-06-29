package com.example.focusflow.repository;

import com.example.focusflow.entity.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    List<TaskAssignment> findByTaskId(Integer taskId);
    void deleteByTaskId(Integer taskId);
}
