package com.task.absensi.presensi;

import com.task.absensi.api.dto.AbseniRequest;
import com.task.absensi.api.dto.PresensiDto;
import com.task.absensi.api.dto.StatusAbsenDto;
import com.task.absensi.api.security.AppUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/presensi")
@Validated
public class PresensiController {
    private final PresensiService presensiService;

    public PresensiController(PresensiService presensiService) {
        this.presensiService = presensiService;
    }

    @GetMapping("/combo/status-absen")
    public List<StatusAbsenDto> comboStatusAbsen(@RequestParam("tglAwal") long tglAwal, @RequestParam("tglAkhir") long tglAkhir) {
        return presensiService.comboStatusAbsen(tglAwal, tglAkhir);
    }

    @GetMapping("/daftar/admin")
    @PreAuthorize("hasAnyRole('ADMIN','HRD')")
    public List<PresensiDto> daftarAdmin(@RequestParam("tglAwal") long tglAwal, @RequestParam("tglAkhir") long tglAkhir) {
        return presensiService.daftarAdmin(tglAwal, tglAkhir);
    }

    @GetMapping("/daftar/pegawai")
    public List<PresensiDto> daftarPegawai(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestParam("tglAwal") long tglAwal,
            @RequestParam("tglAkhir") long tglAkhir
    ) {
        return presensiService.daftarPegawai(userDetails.getUser(), tglAwal, tglAkhir);
    }

    @GetMapping("/in")
    public PresensiDto in(@AuthenticationPrincipal AppUserDetails userDetails) {
        return presensiService.checkIn(userDetails.getUser());
    }

    @GetMapping("/out")
    public PresensiDto out(@AuthenticationPrincipal AppUserDetails userDetails) {
        return presensiService.checkOut(userDetails.getUser());
    }

    @PostMapping("/abseni")
    public PresensiDto abseni(@AuthenticationPrincipal AppUserDetails userDetails, @Valid @RequestBody AbseniRequest request) {
        return presensiService.abseni(userDetails.getUser(), request);
    }
}
