package com.task.absensi.api.dto;

public class TambahPegawaiResponse {
    private String message;
    private String password;
    private UserProfileDto info;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfileDto getInfo() {
        return info;
    }

    public void setInfo(UserProfileDto info) {
        this.info = info;
    }
}
