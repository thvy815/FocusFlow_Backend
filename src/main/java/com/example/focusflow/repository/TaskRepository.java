package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> { 

    // ✅ Task cá nhân (do user tạo, không có phân công nhóm)
    @Query("""
        SELECT t FROM Task t
        WHERE t.userId = :userId
        AND NOT EXISTS (
            SELECT 1 FROM TaskAssignment ta WHERE ta.taskId = t.id
        )
    """)
    List<Task> findPersonalTasksByUserId(@Param("userId") Integer userId);
    
    // Lấy tất cả task nhóm mà user được phân công thông qua bảng TaskAssignment
    @Query("""
        SELECT t FROM Task t
        JOIN TaskAssignment ta ON t.id = ta.taskId
        JOIN CtGroupUser ct ON ta.ctGroupId = ct.id
        WHERE ct.userId = :userId
    """)
    List<Task> findGroupTasksByUserId(@Param("userId") Integer userId);

    // ✅ Task thuộc nhóm nào đó
    @Query("""
        SELECT t FROM Task t
        JOIN TaskAssignment ta ON t.id = ta.taskId
        WHERE ta.ctGroupId IN :ctGroupIds
    """)
    List<Task> findByCtGroupIdIn(@Param("ctGroupIds") List<Integer> ctGroupIds);
}
