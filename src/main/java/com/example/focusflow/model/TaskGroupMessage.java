package com.example.focusflow.model;

import com.example.focusflow.entity.Task;

public class TaskGroupMessage {
    private String action; // created, updated, deleted
    private Task task;

    public TaskGroupMessage(String action, Task task) {
        this.action = action;
        this.task = task;
    }

    public String getAction() {
        return action;
    }

    public Task getTask() {
        return task;
    }
}

