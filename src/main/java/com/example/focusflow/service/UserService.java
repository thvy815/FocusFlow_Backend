package com.example.focusflow.service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Group;
import com.example.focusflow.entity.Pomodoro;
import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.User;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.GroupRepository;
import com.example.focusflow.repository.PomodoroDetailRepository;
import com.example.focusflow.repository.PomodoroRepository;
import com.example.focusflow.repository.StreakRepository;
import com.example.focusflow.repository.TaskAssignmentRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired  
    private UserRepository userRepository;

    @Autowired
    private PomodoroRepository pomodoroRepository;
    
    @Autowired
    private PomodoroDetailRepository pomodoroDetailRepository;

    @Autowired
    private StreakRepository streakRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;

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
    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Integer userId = user.getId();

            // 1. Xóa PomodoroDetail liên quan đến Pomodoro của user
            List<Pomodoro> pomodoros = pomodoroRepository.findByUserId(userId);
            for (Pomodoro pomo : pomodoros) {
                pomodoroDetailRepository.deleteByPomodoroId(pomo.getId());
            }

            // 2. Xóa Pomodoro
            pomodoroRepository.deleteByUserId(userId);

            // 3. Xóa Streak (bao gồm valid dates nhờ @ElementCollection)
            streakRepository.deleteByUserId(userId);

            // 4. Lấy tất cả task do user tạo → xóa TaskAssignment liên quan
            List<Task> tasks = taskRepository.findByUserId(userId);
            for (Task task : tasks) {
                taskAssignmentRepository.deleteByTaskId(task.getId());
            }

            // 5. Xóa Task
            taskRepository.deleteByUserId(userId);

            // 6. Xóa trong bảng ct_group_user (tham gia nhóm)
            ctGroupUserRepository.deleteByUserId(userId);

            // 7. Xóa nhóm nếu chỉ có mình leader;  tự động chuyển quyền cho người khác nếu xóa leader
            List<Group> leaderGroups = groupRepository.findAllGroupsByUserId(userId);

            for (Group group : leaderGroups) {
                int groupId = group.getId();

                // Lấy toàn bộ thành viên trong nhóm
                List<CtGroupUser> allMembers = ctGroupUserRepository.findByGroupId(groupId);

                // Tìm CtGroupUser ứng với leader (để lấy id của leader trong ct_group_user)
                Optional<CtGroupUser> leaderEntryOpt = allMembers.stream()
                    .filter(cg -> cg.getUserId() == userId)
                    .findFirst();

                if (leaderEntryOpt.isEmpty()) {
                    continue; // không tìm thấy leader trong ct_group_user => bỏ qua nhóm này
                }

                int leaderCtGroupUserId = leaderEntryOpt.get().getId();

                // Tìm người có ct_group_user.id nhỏ nhất > leader
                Optional<CtGroupUser> newLeaderOpt = allMembers.stream()
                    .filter(cg -> cg.getUserId() != userId && cg.getId() > leaderCtGroupUserId)
                    .min(Comparator.comparingInt(CtGroupUser::getId));

                if (newLeaderOpt.isPresent()) {
                    CtGroupUser newLeader = newLeaderOpt.get();
                    group.setLeaderId(newLeader.getUserId());
                    groupRepository.save(group); // cập nhật leader mới
                } else {
                    // Không còn ai khác trong nhóm → xóa nhóm
                    groupRepository.delete(group);
                }
            }

            // 8. Xóa user
            userRepository.delete(user);
        }
    }
}
