package com.task.absensi.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StatusAbsen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kdStatus;

    @Column(nullable = false)
    private String namaStatus;

    public Long getId() {
        return id;
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
