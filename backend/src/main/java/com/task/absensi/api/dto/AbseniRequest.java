package com.task.absensi.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AbseniRequest {
    @NotNull
    private Long tglAbsensi;

    @NotBlank
    private String kdStatus;

    public Long getTglAbsensi() {
        return tglAbsensi;
    }

    public void setTglAbsensi(Long tglAbsensi) {
        this.tglAbsensi = tglAbsensi;
    }

    public String getKdStatus() {
        return kdStatus;
    }

    public void setKdStatus(String kdStatus) {
        this.kdStatus = kdStatus;
    }
}
