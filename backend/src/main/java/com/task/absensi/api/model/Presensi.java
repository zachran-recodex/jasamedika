package com.task.absensi.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "presensi",
        uniqueConstraints = @UniqueConstraint(name = "uk_presensi_user_date", columnNames = {"user_id", "tgl_absensi"})
)
public class Presensi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tgl_absensi", nullable = false)
    private LocalDate tglAbsensi;

    private LocalTime jamMasuk;

    private LocalTime jamKeluar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_absen_id")
    private StatusAbsen statusAbsen;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getTglAbsensi() {
        return tglAbsensi;
    }

    public void setTglAbsensi(LocalDate tglAbsensi) {
        this.tglAbsensi = tglAbsensi;
    }

    public LocalTime getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(LocalTime jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public LocalTime getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(LocalTime jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public StatusAbsen getStatusAbsen() {
        return statusAbsen;
    }

    public void setStatusAbsen(StatusAbsen statusAbsen) {
        this.statusAbsen = statusAbsen;
    }
}
