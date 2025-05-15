package com.example.focusflow.model;

public class SignInResponse {

    private String token;
    private int userId;

    // Constructors, getters, setters
    public SignInResponse() {}

    public SignInResponse(String token, int userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
