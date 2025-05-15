package com.example.focusflow.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;

// import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Pomodoro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // private Long userId;
    // private Long taskId;
    private Timestamp createdAt;
    private Timestamp endAt;
    private int totalTime;
    private boolean isDeleted;
    private LocalDate sessionDate;

    @OneToMany(mappedBy = "pomodoro", cascade = CascadeType.ALL)
    private List<PomodoroDetail> details = new ArrayList<>();

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // public Long getUserId() {
    // return userId;
    // }

    // public void setUserId(Long userId) {
    // this.userId = userId;
    // }

    // public Long getTaskId() {
    // return taskId;
    // }

    // public void setTaskId(Long taskId) {
    // this.taskId = taskId;
    // }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sDate) {
        sessionDate = sDate;
    }
}
