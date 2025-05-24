package com.example.focusflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ct_group_user")
public class CtGroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ct")
    private int idCt;

    @Column(name = "group_id", nullable = false)
    private int groupId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    // Constructors
    public CtGroupUser() {}

    public CtGroupUser(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getIdCt() {
        return idCt;
    }

    public void setIdCt(int idCt) {
        this.idCt = idCt;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
