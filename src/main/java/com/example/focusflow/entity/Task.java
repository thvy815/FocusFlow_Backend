package com.example.focusflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "ctgroup_id")
    private Integer ctGroupId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private String dueDate;

    @Column(name = "time")
    private String time;

    @Column(name = "tag")
    private String tag;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "repeat_style")
    private String repeatStyle;

    @Column(name = "reminder_style")
    private String reminderStyle;

    @Column(name = "is_completed", columnDefinition = "BOOLEAN")
    private Boolean isCompleted = false;

    // Constructorsgetters, setters
    public Task() {}

    public Task(Integer id, Integer userId, Integer ctGroupId,  String title, String description,
                String dueDate, String time, String tag, Integer priority, String repeatStyle, String reminderStyle) {
        this.id = id;
        this.userId = userId;
        this.ctGroupId = ctGroupId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.time = time;
        this.tag = tag;
        this.priority = priority;
        this.repeatStyle = repeatStyle;
        this.reminderStyle = reminderStyle;
        this.isCompleted = false;
    }

    // Getters, setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
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
    
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getTag() {
        return tag;
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
}
