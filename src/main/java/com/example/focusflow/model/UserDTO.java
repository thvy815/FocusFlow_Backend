package com.example.focusflow.model;

import com.example.focusflow.entity.User;

public class UserDTO {
    private Integer id;
    private String fullName;
    private String username;
    private String avatarUrl;

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername(); // hoặc getName()
        this.avatarUrl = user.getAvatarUrl(); // nếu có
    }

    // Getters và setters
     public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

     public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

