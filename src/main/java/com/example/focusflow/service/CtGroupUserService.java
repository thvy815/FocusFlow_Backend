package com.example.focusflow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.User;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.GroupRepository;
import com.example.focusflow.repository.UserRepository;

@Service
public class CtGroupUserService {

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    public CtGroupUser addUserToGroup(CtGroupUser ctGroupUser) {
        // Kiểm tra userId có tồn tại không
        if (!userRepository.existsById(ctGroupUser.getUserId())) {
            throw new IllegalArgumentException("User ID does not exist");
        }
        // 🔍 Kiểm tra groupId có tồn tại không
        if (!groupRepository.existsById(ctGroupUser.getGroupId())) {
            throw new IllegalArgumentException("Group ID does not exist.");
        }
        // Kiểm tra xem user đã ở trong group chưa
        boolean exists = ctGroupUserRepository.existsByGroupIdAndUserId(ctGroupUser.getGroupId(), ctGroupUser.getUserId());
        if (exists) {
            throw new IllegalStateException("User is already a member of this group.");
        }

        return ctGroupUserRepository.save(ctGroupUser);
    }

    public CtGroupUser getCtGroupUserById(Integer id) {
        return ctGroupUserRepository.findById(id).orElse(null);
    }

    public List<User> getUsersByGroupId(Integer groupId) {
        List<CtGroupUser> ctList = ctGroupUserRepository.findByGroupId(groupId);
        List<User> users = new ArrayList<>();
        for (CtGroupUser ct : ctList) {
            userRepository.findById(ct.getUserId()).ifPresent(users::add);
        }
        return users;
    }

    public Integer getCtIdByUserIdAndGroupId(Integer userId, Integer groupId) {
        return ctGroupUserRepository.findCtIdByUserIdAndGroupId(userId, groupId);
    }

    public List<CtGroupUser> getUsersInGroup(Integer groupId) {
        return ctGroupUserRepository.findByGroupId(groupId);
    }

    public List<CtGroupUser> getGroupsOfUser(Integer userId) {
        return ctGroupUserRepository.findByUserId(userId);
    }

    public void removeCtGroupUser(Integer idCt) {
        ctGroupUserRepository.deleteById(idCt);
    }

    @Transactional
    public void removeByGroupIdAndUserId(Integer groupId, Integer userId) {
        ctGroupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Transactional
    public void removeAllUsersFromGroup(Integer groupId) {
        ctGroupUserRepository.deleteByGroupId(groupId);
    }
}