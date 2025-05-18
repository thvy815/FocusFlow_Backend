package com.example.focusflow.entity;

import java.sql.Time;
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
    private Time startAt;

    @Column(name = "end_at", nullable = true) // thoi gian ket thuc lam pomodoro
    private Time endAt;

    @Column(name = "due_date") // ngay chay pomodo_id nay
    private LocalDate dueDate;

    @Column(name = "total_time", nullable = true) // tong thoi gian lam pomo
    private long totalTime;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN") // pomo da xoa
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public Pomodoro() {
    }

    public Pomodoro(int id, int userId, int taskId, Time startAt, Time endAt, LocalDate dueDate,
            long totalTime, Boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.dueDate = dueDate;
        this.totalTime = totalTime;
        this.isDeleted = isDeleted;
    }

    public Pomodoro(int id, int userId, int taskId, Time startAt, LocalDate dueDate, Boolean isDeleted) {
        this.userId = userId;
        this.taskId = taskId;
        this.startAt = startAt;
        this.dueDate = dueDate;
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

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
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
