package com.example.focusflow.controller;

import com.example.focusflow.entity.User;
import com.example.focusflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user") // Đường dẫn chung cho các API người dùng
public class UserController {

     @Autowired  // Tiêm phụ thuộc UserService vào UserController
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

    // API để lấy thông tin người dùng theo email
    @GetMapping("/{email}")
    public User getUser(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // API để xóa người dùng theo id
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
