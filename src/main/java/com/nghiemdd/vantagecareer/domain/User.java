package com.nghiemdd.vantagecareer.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nghiemdd.vantagecareer.util.SecurityUtil;
import com.nghiemdd.vantagecareer.util.constant.GenderEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
// Tên bảng trong database
@Table(name = "users")
@Getter
@Setter
public class User {

    // 1. Khóa Chính (Primary Key)
    @Id
    // 2. Tự động tăng giá trị (Auto-Increment)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Password không được để trống")
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}