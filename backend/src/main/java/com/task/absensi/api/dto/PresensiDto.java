package com.task.absensi.api.dto;

public class PresensiDto {
    private Long idUser;
    private String namaLengkap;
    private Long tglAbsensi;
    private String jamMasuk;
    private String jamKeluar;
    private String namaStatus;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public Long getTglAbsensi() {
        return tglAbsensi;
    }

    public void setTglAbsensi(Long tglAbsensi) {
        this.tglAbsensi = tglAbsensi;
    }

    public String getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(String jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public String getNamaStatus() {
        return namaStatus;
    }

    public void setNamaStatus(String namaStatus) {
        this.namaStatus = namaStatus;
    }
}
