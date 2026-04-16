package com.task.absensi.api.dto;

public class StatusAbsenDto {
    private String kdStatus;
    private String namaStatus;

    public StatusAbsenDto() {
    }

    public StatusAbsenDto(String kdStatus, String namaStatus) {
        this.kdStatus = kdStatus;
        this.namaStatus = namaStatus;
    }

    public String getKdStatus() {
        return kdStatus;
    }

    public void setKdStatus(String kdStatus) {
        this.kdStatus = kdStatus;
    }

    public String getNamaStatus() {
        return namaStatus;
    }

    public void setNamaStatus(String namaStatus) {
        this.namaStatus = namaStatus;
    }
}
