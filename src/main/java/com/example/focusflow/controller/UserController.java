package com.example.focusflow.controller;

import com.example.focusflow.entity.User;
import com.example.focusflow.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user") // Đường dẫn chung cho các API người dùng
public class UserController {

    @Autowired  
    private UserService userService;

    // API để reset id = 1
    @PostMapping("/resetId")
    public void resetAutoIncrement() {
        userService.resetAutoIncrement();
    }

     // API để tạo người dùng mới 
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // API để lấy thông tin người dùng (email tự lấy từ JWT)
    @GetMapping("/now")
    public User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserByEmail(email);
    }

    // API để xóa người dùng (email tự lấy từ JWT)
    @DeleteMapping("/now")
    public void deleteCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUserByEmail(email);
    }
}
