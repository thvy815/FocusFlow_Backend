package com.example.focusflow.service;

import com.example.focusflow.entity.User;
import com.example.focusflow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired  // Tiêm phụ thuộc UserRepository vào UserService
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    // Phương thức reset lại AUTO_INCREMENT = 1
    @Transactional
    public void resetAutoIncrement() {
        entityManager.createNativeQuery("ALTER TABLE user AUTO_INCREMENT = 1").executeUpdate();
    }

    // Phương thức trả về người dùng theo tên đăng nhập
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsernameOrEmail(String input) {
        User user = userRepository.findByUsername(input);
        if (user == null) {
            user = userRepository.findByEmail(input);
        }
        return user;
    }

    // Phương thức trả về người dùng theo id
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Phương thức kiểm tra mật khẩu người dùng
    public boolean checkPassword(User user, String password) {
        return user != null && user.getPassword().equals(password);
    }

    // Phương thức lưu người dùng vào cơ sở dữ liệu
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Phương thức xóa người dùng theo email
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
