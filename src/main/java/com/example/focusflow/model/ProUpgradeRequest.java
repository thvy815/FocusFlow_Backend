package com.example.focusflow.model;

// Không còn email
public class ProUpgradeRequest {
    private String planName;
    private Long expireTime;

    public ProUpgradeRequest() {
    }

    public ProUpgradeRequest(String planName, Long expireTime) {
        this.planName = planName;
        this.expireTime = expireTime;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
