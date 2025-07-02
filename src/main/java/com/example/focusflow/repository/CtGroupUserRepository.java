package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.CtGroupUser;

@Repository
public interface CtGroupUserRepository extends JpaRepository<CtGroupUser, Integer> {
    List<CtGroupUser> findByGroupId(Integer groupId);

    List<CtGroupUser> findByUserId(Integer userId);

    @Query("SELECT c.id FROM CtGroupUser c WHERE c.groupId = :groupId")
    List<Integer> findCtGroupIdByGroupId(@Param("groupId") Integer groupId);

    boolean existsByGroupIdAndUserId(Integer groupId, Integer userId);

    @Query("SELECT c.id FROM CtGroupUser c WHERE c.userId = :userId AND c.groupId = :groupId")
    Integer findCtIdByUserIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    void deleteByGroupIdAndUserId(Integer groupId, Integer userId);
    void deleteByGroupId(Integer groupId);
    void deleteByUserId(Integer userId);

    @Query("SELECT c.id FROM CtGroupUser c WHERE c.groupId = :groupId AND c.userId IN (:userIds)")
    List<Integer> findCtIdsByUserIdsAndGroupId(@Param("userIds") List<Integer> userIds, @Param("groupId") Integer groupId);
}