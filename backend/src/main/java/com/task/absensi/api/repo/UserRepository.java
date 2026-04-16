package com.task.absensi.api.repo;

import com.task.absensi.api.model.Role;
import com.task.absensi.api.model.User;
import com.task.absensi.api.model.Departemen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByDepartemen(Departemen departemen);
}
