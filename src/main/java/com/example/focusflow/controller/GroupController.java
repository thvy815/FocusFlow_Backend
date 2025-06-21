package com.example.focusflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Group;
import com.example.focusflow.model.GroupWithUsersRequest;
import com.example.focusflow.service.CtGroupUserService;
import com.example.focusflow.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;
    private final CtGroupUserService ctGroupUserService; 

    public GroupController(GroupService groupService, CtGroupUserService ctGroupUserService) {
        this.groupService = groupService;
        this.ctGroupUserService = ctGroupUserService;
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PostMapping("/create-with-users")
    public ResponseEntity<?> createGroupWithUsers(@RequestBody GroupWithUsersRequest request) {
        // Tạo nhóm
        Group group = new Group();
        group.setGroupName(request.getGroupName());
        group.setLeaderId(request.getLeaderId());
        
        Group savedGroup = groupService.createGroup(group);

        // Thêm user vào nhóm
        for (Integer userId : request.getUserIds()) {
            CtGroupUser ct = new CtGroupUser();
            ct.setGroupId(savedGroup.getId());
            ct.setUserId(userId);
            ctGroupUserService.addUserToGroup(ct);
        }

        return ResponseEntity.ok(savedGroup);
    }

    @Transactional
    @PostMapping("/{groupId}/members")
    public ResponseEntity<String> addMembersToGroup(@PathVariable Integer groupId, @RequestBody List<Integer> userIds) {
        for (Integer userId : userIds) {
            CtGroupUser ct = new CtGroupUser();
            ct.setGroupId(groupId);
            ct.setUserId(userId);
            ctGroupUserService.addUserToGroup(ct);
        }

        return ResponseEntity.ok("Members added successfully");
    }

    @GetMapping("/user/{userId}")
    public List<Group> getGroupsOfUser(@PathVariable Integer userId) {
        return groupService.getAllGroupsByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Integer id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        ctGroupUserService.removeAllUsersFromGroup(id);  // Xóa bảng trung gian trước
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}