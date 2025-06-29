package com.example.focusflow.model;

import java.util.List;

public class CtIdRequest {
    private List<Integer> userIds;
    private Integer groupId;

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}