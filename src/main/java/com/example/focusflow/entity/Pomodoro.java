package com.example.focusflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pomodoro")
public class Pomodoro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false) // nguoi dung lam pomodoro
    private Integer userId;

    @Column(name = "task_id") // task dung pomodoro
    private Integer taskId;

    @Column(name = "start_at") // thoi gian bat dau lam pomodoro
    private String startAt;

    @Column(name = "end_at") // thoi gian ket thuc lam pomodoro
    private String endAt;

    @Column(name = "due_date") // ngay chay pomodo_id nay
    private String dueDate;

    @Column(name = "total_time") // tong thoi gian lam pomo
    private Integer totalTime;

    // Constructorsgetters, setters
    public Pomodoro() {
    }

    public Pomodoro(int id, int userId, int taskId, String startAt, String endAt, String dueDate,
            int totalTime) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.dueDate = dueDate;
        this.totalTime = totalTime;
    }
    // Getter, setter

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }
}
