package com.example.focusflow.repository;

import com.example.focusflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Phương thức tìm người dùng theo tên đăng nhập (username).
    User findByUsername(String username);
}
