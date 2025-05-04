package com.example.focusflow.model;

public class SignInResponse {

    private String token;

    // Constructors, getters, setters
    public SignInResponse() {}

    public SignInResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
