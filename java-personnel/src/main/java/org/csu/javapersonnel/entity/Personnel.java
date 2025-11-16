package org.csu.javapersonnel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@TableName("personnel")
public class Personnel {

    @TableId(type = IdType.AUTO)
    private Integer personnelId;

    private String name;
    private String department;
    private String role;
    private String contact;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== Getter / Setter =====

    public Integer getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(Integer personnelId) {
        this.personnelId = personnelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== Optional: toString() (方便打印) =====
    @Override
    public String toString() {
        return "Personnel{" +
                "personnelId=" + personnelId +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", role='" + role + '\'' +
                ", contact='" + contact + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

