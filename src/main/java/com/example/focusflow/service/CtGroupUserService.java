package com.example.focusflow.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Group;
import com.example.focusflow.entity.TaskAssignment;
import com.example.focusflow.entity.User;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.GroupRepository;
import com.example.focusflow.repository.TaskAssignmentRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;

@Service
public class CtGroupUserService {

    @Autowired
    private CtGroupUserRepository ctGroupUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;


    public CtGroupUser addUserToGroup(CtGroupUser ctGroupUser) {
        // Kiểm tra userId có tồn tại không
        if (!userRepository.existsById(ctGroupUser.getUserId())) {
            throw new IllegalArgumentException("User ID does not exist");
        }
        // 🔍 Kiểm tra groupId có tồn tại không
        if (!groupRepository.existsById(ctGroupUser.getGroupId())) {
            throw new IllegalArgumentException("Group ID does not exist.");
        }
        // Kiểm tra xem user đã ở trong group chưa
        boolean exists = ctGroupUserRepository.existsByGroupIdAndUserId(ctGroupUser.getGroupId(), ctGroupUser.getUserId());
        if (exists) {
            throw new IllegalStateException("User is already a member of this group.");
        }

        return ctGroupUserRepository.save(ctGroupUser);
    }

    public CtGroupUser getCtGroupUserById(Integer id) {
        return ctGroupUserRepository.findById(id).orElse(null);
    }

    public List<User> getUsersByGroupId(Integer groupId) {
        List<CtGroupUser> ctList = ctGroupUserRepository.findByGroupId(groupId);
        List<User> users = new ArrayList<>();
        for (CtGroupUser ct : ctList) {
            userRepository.findById(ct.getUserId()).ifPresent(users::add);
        }
        return users;
    }

    public Integer getCtIdByUserIdAndGroupId(Integer userId, Integer groupId) {
        return ctGroupUserRepository.findCtIdByUserIdAndGroupId(userId, groupId);
    }

    public List<CtGroupUser> getUsersInGroup(Integer groupId) {
        return ctGroupUserRepository.findByGroupId(groupId);
    }

    public List<CtGroupUser> getGroupsOfUser(Integer userId) {
        return ctGroupUserRepository.findByUserId(userId);
    }

    public void removeCtGroupUser(Integer idCt) {
        ctGroupUserRepository.deleteById(idCt);
    }

    public List<Integer> getCtIdsForUsersInGroup(List<Integer> userIds, Integer groupId) {
        return ctGroupUserRepository.findCtIdsByUserIdsAndGroupId(userIds, groupId);
    }

    @Transactional
    public void removeByGroupIdAndUserId(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return;

        // Nếu user là leader
        if (userId != null && group.getLeaderId() == userId) {
            List<CtGroupUser> allMembers = ctGroupUserRepository.findByGroupId(groupId);

            // Loại bỏ chính leader
            List<CtGroupUser> otherMembers = allMembers.stream()
                .filter(cg -> cg.getUserId() != userId.intValue())
                .toList();

            if (otherMembers.isEmpty()) {
                // ⚠️ Không còn ai khác trong nhóm → phải xóa tất cả Task và TaskAssignment trước khi xóa Group

                // Lấy các task của group
                List<TaskAssignment> taskAssignments = taskAssignmentRepository.findByGroupId(groupId);
                for (TaskAssignment taskAssignment : taskAssignments) {
                    Integer taskId = taskAssignment.getTaskId();
                
                    // Xóa assignment trước
                    taskAssignmentRepository.deleteByTaskId(taskId);

                    // Xóa task
                    taskRepository.deleteById(taskId); 
                }

                // Xóa group
                groupRepository.delete(group);
            } else {
                // 🔁 Chọn người có ct_group_user.id nhỏ nhất để làm leader mới
                CtGroupUser newLeader = otherMembers.stream()
                        .min(Comparator.comparingInt(CtGroupUser::getId))
                        .get();

                group.setLeaderId(newLeader.getUserId());
                groupRepository.save(group); // cập nhật leader mới
            }
        }

        // ❌ Sau cùng mới xóa khỏi bảng ct_group_user
        ctGroupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Transactional
    public void removeAllUsersFromGroup(Integer groupId) {
        ctGroupUserRepository.deleteByGroupId(groupId);
    }
}