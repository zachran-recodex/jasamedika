package com.task.absensi.api.dto;

import javax.validation.constraints.NotBlank;

public class UbahPasswordRequest {
    @NotBlank
    private String passwordAsli;

    @NotBlank
    private String passwordBaru1;

    @NotBlank
    private String passwordBaru2;

    public String getPasswordAsli() {
        return passwordAsli;
    }

    public void setPasswordAsli(String passwordAsli) {
        this.passwordAsli = passwordAsli;
    }

    public String getPasswordBaru1() {
        return passwordBaru1;
    }

    public void setPasswordBaru1(String passwordBaru1) {
        this.passwordBaru1 = passwordBaru1;
    }

    public String getPasswordBaru2() {
        return passwordBaru2;
    }

    public void setPasswordBaru2(String passwordBaru2) {
        this.passwordBaru2 = passwordBaru2;
    }
}
