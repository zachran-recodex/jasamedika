package com.task.absensi.api.repo;

import com.task.absensi.api.model.Perusahaan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerusahaanRepository extends JpaRepository<Perusahaan, Long> {
    boolean existsByNama(String nama);
}
