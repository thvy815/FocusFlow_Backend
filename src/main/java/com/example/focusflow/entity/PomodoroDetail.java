package com.example.focusflow.entity;

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

    @Column(name = "pomodoro_id", nullable = false) // id pomodoro chua details
    private Integer pomodoroId;

    @Column(name = "start_at") // thoi gian bat dau
    private String startAt;

    @Column(name = "end_at") // thoi gian ket thuc
    private String endAt;

    @Column(name = "total_time") // tong thoi gian lam detail
    private Integer totalTime;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN") // pomo da xoa
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public PomodoroDetail() {
    }

    public PomodoroDetail(int id, int pomodoroId, String startAt, String endAt, int totalTime) {
        this.id = id;
        this.pomodoroId = pomodoroId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalTime = totalTime;
    }
    // Getter, setter

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPomodoroId(int pomodoroId) {
        this.pomodoroId = pomodoroId;
    }

    public int getPomodoroId() {
        return pomodoroId;
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

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setDeleted(boolean is_deleted) {
        this.isDeleted = is_deleted;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }
}
