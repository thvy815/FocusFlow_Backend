package com.example.focusflow.model;

public class SignInRequest {

    private String emailOrUsername;
    private String password;
    private boolean rememberMe;

    // Constructors, getters, setters
    public SignInRequest() {}

    public SignInRequest(String emailorUsername, String password, boolean rememberMe) {
        this.emailOrUsername = emailorUsername;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}