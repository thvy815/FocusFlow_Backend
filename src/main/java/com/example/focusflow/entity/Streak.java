package com.example.focusflow.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "streak")
public class Streak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "last_valid_date")
    private String lastValidDate;

    @Column(name = "current_streak")
    private int currentStreak = 0;

    @Column(name = "max_streak")
    private int maxStreak = 0;

    @ElementCollection
    @CollectionTable(name = "streak_valid_dates", joinColumns = @JoinColumn(name = "streak_id"))
    @Column(name = "valid_date")
    private List<String> validDates = new ArrayList<>();

    // Constructors
    public Streak() {
    }

    public Streak(Integer userId, String lastValidDate, int currentStreak, int maxStreak) {
        this.userId = userId;
        this.lastValidDate = lastValidDate;
        this.currentStreak = currentStreak;
        this.maxStreak = maxStreak;
    }

    // Getters & Setters
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

    public String getLastValidDate() {
        return lastValidDate;
    }

    public void setLastValidDate(String lastValidDate) {
        this.lastValidDate = lastValidDate;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public List<String> getValidDates() {
        return validDates;
    }

    public void setValidDates(List<String> validDates) {
        this.validDates = validDates;
    }
}
