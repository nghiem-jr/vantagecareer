package com.nghiemdd.vantagecareer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
    private String access_token;
    private UserLogin user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
    }
    // public ResLoginDTO(String access_token) {
    // this.access_token = access_token;
    // }

    // public String getAccess_token() {
    // return access_token;
    // }

    // public void setAccess_token(String access_token) {
    // this.access_token = access_token;
    // }
}
