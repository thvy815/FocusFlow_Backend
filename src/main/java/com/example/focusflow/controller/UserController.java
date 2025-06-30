package com.example.focusflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.User;
import com.example.focusflow.service.UserService;

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

    // API để lấy user theo id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // API để lấy tất cả người dùng
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // API để cập nhật thông tin người dùng (email lấy từ JWT)
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUserData) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User existingUser = userService.getUserByEmail(email);

        // Cập nhật các trường được cho phép
        existingUser.setFullName(updatedUserData.getFullName());
        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setAvatarUrl(updatedUserData.getAvatarUrl()); // nếu có avatar
        
        User updatedUser = userService.saveUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // API để xóa người dùng (email tự lấy từ JWT)
    @DeleteMapping("/now")
    public void deleteCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUserByEmail(email);
    }
}
