package com.example.focusflow.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

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

    @Column(name = "task_id", nullable = false) // task dung pomodoro
    private Integer taskId;

    @Column(name = "start_at") // thoi gian bat dau lam pomodoro
    private Timestamp startAt;

    @Column(name = "end_at") // thoi gian ket thuc lam pomodoro
    private Timestamp endAt;

    @Column(name = "due_date") // ngay chay pomodo_id nay
    private LocalDate dueDate;

    @Column(name = "total_time") // tong thoi gian lam pomo
    private Integer totalTime;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN") // pomo da xoa
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public Pomodoro() {
    }

    public Pomodoro(int id, int userId, int taskId, Timestamp startAt, Timestamp endAt, LocalDate dueDate,
            int totalTime, Boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.dueDate = dueDate;
        this.totalTime = totalTime;
        this.isDeleted = isDeleted;
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

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getDeleted() {
        return isDeleted;
    }

}
