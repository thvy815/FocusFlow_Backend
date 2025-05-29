package com.example.focusflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
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

    public List<CtGroupUser> getUsersInGroup(Integer groupId) {
        return ctGroupUserRepository.findByGroupId(groupId);
    }

    public List<CtGroupUser> getGroupsOfUser(Integer userId) {
        return ctGroupUserRepository.findByUserId(userId);
    }

    public void removeCtGroupUser(Integer idCt) {
        ctGroupUserRepository.deleteById(idCt);
    }
}