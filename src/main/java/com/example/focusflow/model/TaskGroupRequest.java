package com.example.focusflow.model;

import java.util.List;

public class TaskGroupRequest {
    public Integer taskId; 
    public Integer userId;
    public String title;
    public String description;
    public String dueDate;
    public String time;
    public String tag;
    public Integer priority;
    public String repeatStyle;
    public String reminderStyle;
    
    // Optional: có thì là nhóm, không có thì là cá nhân
    public List<Integer> ctGroupIds;
}
