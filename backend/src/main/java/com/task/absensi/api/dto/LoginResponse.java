package com.task.absensi.api.dto;

public class LoginResponse {
    private String token;
    private UserProfileDto info;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfileDto getInfo() {
        return info;
    }

    public void setInfo(UserProfileDto info) {
        this.info = info;
    }
}
