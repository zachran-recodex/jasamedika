package com.task.absensi.api.dto;

import javax.validation.constraints.NotNull;

public class AdminUbahPegawaiRequest {
    @NotNull
    private Long idUser;

    private String namaLengkap;
    private String tempatLahir;
    private Long tglLahir;
    private String email;

    private Long idJabatan;
    private Long idDepartemen;
    private Long idUnitKerja;
    private Long idJenisKelamin;
    private Long idPendidikan;

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

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public Long getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(Long tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(Long idJabatan) {
        this.idJabatan = idJabatan;
    }

    public Long getIdDepartemen() {
        return idDepartemen;
    }

    public void setIdDepartemen(Long idDepartemen) {
        this.idDepartemen = idDepartemen;
    }

    public Long getIdUnitKerja() {
        return idUnitKerja;
    }

    public void setIdUnitKerja(Long idUnitKerja) {
        this.idUnitKerja = idUnitKerja;
    }

    public Long getIdJenisKelamin() {
        return idJenisKelamin;
    }

    public void setIdJenisKelamin(Long idJenisKelamin) {
        this.idJenisKelamin = idJenisKelamin;
    }

    public Long getIdPendidikan() {
        return idPendidikan;
    }

    public void setIdPendidikan(Long idPendidikan) {
        this.idPendidikan = idPendidikan;
    }
}
