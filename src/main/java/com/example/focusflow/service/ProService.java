package com.example.focusflow.service;

import com.example.focusflow.entity.User;
import com.example.focusflow.model.ProUpgradeRequest;
import com.example.focusflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProService {

    @Autowired
    private UserRepository userRepository;

    // ⚠️ Không lấy email từ request nữa, mà từ controller truyền vào
    public boolean upgradeUserPro(String email, ProUpgradeRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }

        user.setIsPro(true);
        user.setPlanName(request.getPlanName());
        user.setExpireTime(request.getExpireTime());

        userRepository.save(user);
        return true;
    }

    // ⚙️ Lấy trạng thái Pro theo email
    public User getProStatus(String email) {
        return userRepository.findByEmail(email);
    }
}
