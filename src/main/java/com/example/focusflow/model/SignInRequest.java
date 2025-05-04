package com.example.focusflow.model;

public class SignInRequest {

    private String email;
    private String password;

    // Constructors, getters, setters
    public SignInRequest() {}

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}