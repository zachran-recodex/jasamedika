package com.task.absensi.api.repo;

import com.task.absensi.api.model.StatusAbsen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusAbsenRepository extends JpaRepository<StatusAbsen, Long> {
    Optional<StatusAbsen> findByKdStatus(String kdStatus);
}
