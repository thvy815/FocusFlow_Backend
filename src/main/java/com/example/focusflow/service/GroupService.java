package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Group;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.GroupRepository;
import com.example.focusflow.repository.UserRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;

    public Group createGroup(Group group) {
        
        // Kiểm tra leaderId có tồn tại không
        if (!userRepository.existsById(group.getLeaderId())) {
            throw new IllegalArgumentException("Leader ID does not exist in User table");
        }
        

        Group savedGroup = groupRepository.save(group);

        // Sau khi tạo nhóm, thêm leaderId vào bảng ct_group_user
        CtGroupUser leaderLink = new CtGroupUser();
        leaderLink.setGroupId(savedGroup.getId());
        leaderLink.setUserId(savedGroup.getLeaderId());
        ctGroupUserRepository.save(leaderLink);

        return savedGroup;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Integer id) {
        return groupRepository.findById(id);
    }

    public void deleteGroup(Integer id) {
        groupRepository.deleteById(id);
    }
}
