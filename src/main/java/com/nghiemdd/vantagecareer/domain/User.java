package com.nghiemdd.vantagecareer.domain;

import java.time.Instant;

import com.nghiemdd.vantagecareer.util.constant.GenderEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String email;
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

}