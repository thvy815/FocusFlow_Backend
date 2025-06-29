package com.example.focusflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_assignment")
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "ctgroup_id", nullable = false)
    private Integer ctGroupId;

    // Constructors
    public TaskAssignment() {}

    public TaskAssignment(Integer taskId, Integer ctGroupId) {
        this.taskId = taskId;
        this.ctGroupId = ctGroupId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getCtGroupId() {
        return ctGroupId;
    }

    public void setCtGroupId(Integer ctGroupId) {
        this.ctGroupId = ctGroupId;
    }
}
