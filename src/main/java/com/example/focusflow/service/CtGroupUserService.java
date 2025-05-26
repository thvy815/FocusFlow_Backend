package com.example.focusflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.UserRepository;

@Service
public class CtGroupUserService {

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;

    @Autowired
    private UserRepository userRepository;

    public CtGroupUser addUserToGroup(CtGroupUser ctGroupUser) {
        // Kiểm tra userId có tồn tại không
        if (!userRepository.existsById(ctGroupUser.getUserId())) {
            throw new IllegalArgumentException("User ID does not exist");
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