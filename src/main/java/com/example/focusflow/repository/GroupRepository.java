package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE g.id IN (SELECT c.groupId FROM CtGroupUser c WHERE c.userId = :userId)")
    List<Group> findAllGroupsByUserId(@Param("userId") Integer userId);
}