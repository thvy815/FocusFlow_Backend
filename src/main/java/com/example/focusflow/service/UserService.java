package com.example.focusflow.service;

import com.example.focusflow.entity.User;
import com.example.focusflow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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

    // Phương thức lưu người dùng vào cơ sở dữ liệu
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Phương thức xóa người dùng theo id
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
