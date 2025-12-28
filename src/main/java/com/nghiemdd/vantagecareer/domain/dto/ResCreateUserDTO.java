package com.nghiemdd.vantagecareer.domain.dto;

import java.time.Instant;

import com.nghiemdd.vantagecareer.util.constant.GenderEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender; // Lưu ý: Ở đây dùng type là GenderEnum
    private String address;
    private int age;
    private Instant createdAt;
}