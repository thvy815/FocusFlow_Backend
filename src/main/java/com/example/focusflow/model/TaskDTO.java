package com.example.focusflow.model;

import java.util.List;

import com.example.focusflow.entity.Task;

public class TaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String dueDate;
    private String tag;
    private Integer priority;
    private String repeatStyle;
    private String reminderStyle;
    private Boolean isCompleted = false;
    private List<UserDTO> assignedUsers;

    public TaskDTO(Task task, List<UserDTO> assignedUsers) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.dueDate = task.getDueDate();
        this.tag = task.getTag();
        this.priority = task.getPriority();
        this.repeatStyle = task.getRepeatStyle();
        this.reminderStyle = task.getReminderStyle();
        this.isCompleted = task.getIsCompleted();
        this.assignedUsers = assignedUsers;
    }

    // getters và setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getRepeatStyle() {
        return repeatStyle;
    }

    public void setRepeatStyle(String repeatStyle) {
        this.repeatStyle = repeatStyle;
    }

    public String getReminderStyle() {
        return reminderStyle;
    }

    public void setReminderStyle(String reminderStyle) {
        this.reminderStyle = reminderStyle;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<UserDTO> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UserDTO> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
}

