package com.example.focusflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.service.CtGroupUserService;

@RestController
@RequestMapping("/api/group-users")
public class CtGroupUserController {

    @Autowired
    private CtGroupUserService ctGroupUserService;

    @PostMapping
    public CtGroupUser addUserToGroup(@RequestBody CtGroupUser ctGroupUser) {
        return ctGroupUserService.addUserToGroup(ctGroupUser);
    }

    @GetMapping("/group/{groupId}")
    public List<CtGroupUser> getUsersInGroup(@PathVariable Integer groupId) {
        return ctGroupUserService.getUsersInGroup(groupId);
    }

    @GetMapping("/user/{userId}")
    public List<CtGroupUser> getGroupsOfUser(@PathVariable Integer userId) {
        return ctGroupUserService.getGroupsOfUser(userId);
    }

    @DeleteMapping("/{idCt}")
    public void deleteCtGroupUser(@PathVariable Integer idCt) {
        ctGroupUserService.removeCtGroupUser(idCt);
    }
}