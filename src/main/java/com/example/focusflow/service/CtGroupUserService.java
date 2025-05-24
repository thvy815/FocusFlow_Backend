package com.example.focusflow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.repository.CtGroupUserRepository;

@Service
public class CtGroupUserService {

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;

    public CtGroupUser addUserToGroup(CtGroupUser ctGroupUser) {
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