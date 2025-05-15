package com.example.focusflow.entity;

import java.time.LocalDate;

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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "reminder_date")
    private LocalDate reminderDate;

    @Column(name = "is_completed", columnDefinition = "BOOLEAN")
    private Boolean isCompleted = false;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "repeat_style")
    private String repeatStyle;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN")
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public Task() {}

    public Task(Integer id, Integer userId, String title, String description,
            LocalDate dueDate, LocalDate reminderDate, Integer priority, String repeatStyle) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.reminderDate = reminderDate;
        this.isCompleted = false;
        this.priority = priority;
        this.repeatStyle = repeatStyle;
        this.isDeleted = false;
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
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReminderDate() {
        return reminderDate;
    }
    
    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
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
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    } 
}
