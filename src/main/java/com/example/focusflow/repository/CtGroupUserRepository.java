package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.CtGroupUser;

@Repository
public interface CtGroupUserRepository extends JpaRepository<CtGroupUser, Integer> {
    List<CtGroupUser> findByGroupId(Integer groupId);
    List<CtGroupUser> findByUserId(Integer userId);
}