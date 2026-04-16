package com.task.absensi.api.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String namaLengkap;

    private String tempatLahir;

    private LocalDate tglLahir;

    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jabatan_id")
    private Jabatan jabatan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departemen_id")
    private Departemen departemen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_kerja_id")
    private UnitKerja unitKerja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jenis_kelamin_id")
    private JenisKelamin jenisKelamin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pendidikan_id")
    private Pendidikan pendidikan;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;

    private String photoContentType;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public LocalDate getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(LocalDate tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }

    public Departemen getDepartemen() {
        return departemen;
    }

    public void setDepartemen(Departemen departemen) {
        this.departemen = departemen;
    }

    public UnitKerja getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(UnitKerja unitKerja) {
        this.unitKerja = unitKerja;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public Pendidikan getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(Pendidikan pendidikan) {
        this.pendidikan = pendidikan;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }
}
