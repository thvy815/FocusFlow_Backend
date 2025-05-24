package com.example.focusflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}