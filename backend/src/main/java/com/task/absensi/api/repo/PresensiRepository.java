package com.task.absensi.api.repo;

import com.task.absensi.api.model.Presensi;
import com.task.absensi.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PresensiRepository extends JpaRepository<Presensi, Long> {
    Optional<Presensi> findByUserAndTglAbsensi(User user, LocalDate tglAbsensi);

    List<Presensi> findByTglAbsensiBetween(LocalDate tglAwal, LocalDate tglAkhir);

    List<Presensi> findByUserAndTglAbsensiBetween(User user, LocalDate tglAwal, LocalDate tglAkhir);
}
