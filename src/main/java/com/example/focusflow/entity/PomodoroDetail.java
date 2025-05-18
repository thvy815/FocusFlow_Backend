package com.example.focusflow.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pomodoroDetail")
public class PomodoroDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false) // id pomodoro chua details
    private Integer userId;

    @Column(name = "task_id", nullable = false) // id pomodoro chua details
    private Integer taskId;

    @Column(name = "pomodoro_id", nullable = false) // id pomodoro chua details
    private Integer pomodoroId;

    @Column(name = "start_at") // thoi gian bat dau
    private Time startAt;

    @Column(name = "end_at") // thoi gian ket thuc
    private Time endAt;

    @Column(name = "total_time") // tong thoi gian lam detail
    private long totalTime;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN") // pomo da xoa
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public PomodoroDetail() {
    }

    public PomodoroDetail(int id, int userId, int taskId, int pomodoroId, Time startAt, Time endAt, long totalTime,
            boolean is_deleted) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.pomodoroId = pomodoroId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalTime = totalTime;
        this.isDeleted = is_deleted;
    }
    // Getter, setter

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setPomodoroId(int pomodoroId) {
        this.pomodoroId = pomodoroId;
    }

    public int getPomodoroId() {
        return pomodoroId;
    }

    public void setStartAt(Time startAt) {
        this.startAt = startAt;
    }

    public Time getStartAt() {
        return startAt;
    }

    public void setEndAt(Time endAt) {
        this.endAt = endAt;
    }

    public Time getEndAt() {
        return endAt;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

}
