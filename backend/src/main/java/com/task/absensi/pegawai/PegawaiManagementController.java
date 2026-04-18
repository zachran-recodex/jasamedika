package com.task.absensi.pegawai;

import com.task.absensi.api.dto.AdminTambahPegawaiRequest;
import com.task.absensi.api.dto.AdminUbahPegawaiRequest;
import com.task.absensi.api.dto.TambahPegawaiResponse;
import com.task.absensi.api.dto.UbahPhotoRequest;
import com.task.absensi.api.dto.UserProfileDto;
import com.task.absensi.api.security.AppUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class PegawaiManagementController {
    private final PegawaiService pegawaiService;

    public PegawaiManagementController(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

    @GetMapping("/api/pegawai/daftar")
    @PreAuthorize("hasAnyRole('ADMIN','HRD')")
    public List<UserProfileDto> daftar() {
        return pegawaiService.daftarPegawai();
    }

    @PostMapping("/api/pegawai/admin-tambah-pegawai")
    @PreAuthorize("hasAnyRole('ADMIN','HRD')")
    public TambahPegawaiResponse adminTambahPegawai(@Valid @RequestBody AdminTambahPegawaiRequest request) {
        return pegawaiService.adminTambahPegawai(request);
    }

    @PostMapping("/api/pegawai/admin-ubah-pegawai")
    @PreAuthorize("hasAnyRole('ADMIN','HRD')")
    public UserProfileDto adminUbahPegawai(@Valid @RequestBody AdminUbahPegawaiRequest request) {
        return pegawaiService.adminUbahPegawai(request);
    }

    @PostMapping("/api/pegawai/admin-ubah-photo")
    @PreAuthorize("hasAnyRole('ADMIN','HRD')")
    public UserProfileDto adminUbahPhoto(@RequestParam("idUser") Long idUser, @Valid @RequestBody UbahPhotoRequest request) {
        return pegawaiService.adminUbahPhoto(idUser, request);
    }

    @PostMapping("/api/pegawai/ubah-photo")
    @PreAuthorize("hasRole('PEGAWAI')")
    public UserProfileDto ubahPhoto(@AuthenticationPrincipal AppUserDetails userDetails, @Valid @RequestBody UbahPhotoRequest request) {
        return pegawaiService.ubahPhotoSendiri(userDetails.getUser(), request);
    }
}
