package com.task.absensi.api.repo;

import com.task.absensi.api.model.Departemen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartemenRepository extends JpaRepository<Departemen, Long> {
    Optional<Departemen> findByNama(String nama);
}
