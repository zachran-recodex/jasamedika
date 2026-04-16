package com.task.absensi.api.dto;

import javax.validation.constraints.NotBlank;

public class InitDataRequest {
    @NotBlank
    private String namaAdmin;

    @NotBlank
    private String perusahaan;

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getPerusahaan() {
        return perusahaan;
    }

    public void setPerusahaan(String perusahaan) {
        this.perusahaan = perusahaan;
    }
}
