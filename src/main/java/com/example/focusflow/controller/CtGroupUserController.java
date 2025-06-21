package com.example.focusflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.User;
import com.example.focusflow.service.CtGroupUserService;

@RestController
@RequestMapping("/api/group-user")
public class CtGroupUserController {

    @Autowired
    private CtGroupUserService ctGroupUserService;

    @PostMapping
    public CtGroupUser addUserToGroup(@RequestBody CtGroupUser ctGroupUser) {
        return ctGroupUserService.addUserToGroup(ctGroupUser);
    }

    @GetMapping("/{id}")
    public CtGroupUser getCtGroupUserById(@PathVariable Integer id) {
        return ctGroupUserService.getCtGroupUserById(id);
    }

    @GetMapping("/group/{groupId}")
    public List<CtGroupUser> getUsersInGroup(@PathVariable Integer groupId) {
        return ctGroupUserService.getUsersInGroup(groupId);
    }

    @GetMapping("/group/{groupId}/users")
    public List<User> getUserDetailsInGroup(@PathVariable Integer groupId) {
        return ctGroupUserService.getUsersByGroupId(groupId);
    }

    @GetMapping("/user/{userId}")
    public List<CtGroupUser> getGroupsOfUser(@PathVariable Integer userId) {
        return ctGroupUserService.getGroupsOfUser(userId);
    }

    // Lấy ct_id theo userId và groupId
    @GetMapping("/getCtId")
    public ResponseEntity<?> getCtIdByUserAndGroup(@RequestParam Integer userId, @RequestParam Integer groupId) {
        Integer ctId = ctGroupUserService.getCtIdByUserIdAndGroupId(userId, groupId);
        if (ctId != null) {
            return ResponseEntity.ok(ctId);
        } else {
            return ResponseEntity.badRequest().body(0); // Không tìm thấy
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCtGroupUser(@PathVariable Integer idCt) {
        ctGroupUserService.removeCtGroupUser(idCt);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<String> removeUserFromGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        ctGroupUserService.removeByGroupIdAndUserId(groupId, userId);
        return ResponseEntity.ok("User removed from group");
    }
}