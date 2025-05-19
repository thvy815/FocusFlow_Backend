package com.example.focusflow.entity;

import java.security.Timestamp;

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
    private Timestamp startAt;

    @Column(name = "end_at") // thoi gian ket thuc
    private Timestamp endAt;

    @Column(name = "total_time") // tong thoi gian lam detail
    private Integer totalTime;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN") // pomo da xoa
    private Boolean isDeleted = false;

    // Constructorsgetters, setters
    public PomodoroDetail() {
    }

    public PomodoroDetail(int id, int pomodoroId, Timestamp startAt, Timestamp endAt, int totalTime) {
        this.id = id;
        this.pomodoroId = pomodoroId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.totalTime = totalTime;
    }
    // Getter, setter

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public void setPomodoroId(int pomodoroId) {
        this.pomodoroId = pomodoroId;
    }

    public int getPomodoroId() {
        return pomodoroId;
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
