package com.example.focusflow.model;

import java.util.List;

public class PomodoroRequest {
    // public Long userId;
    // public Long taskId;
    public String createdAt;
    public String endAt;
    public int totalTime;
    public List<DetailDTO> details;

    public static class DetailDTO {
        public int durations;
        public String startTime;
        public String endTime;
    }
}
