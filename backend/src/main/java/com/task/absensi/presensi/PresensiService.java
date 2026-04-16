package com.task.absensi.presensi;

import com.task.absensi.api.dto.AbseniRequest;
import com.task.absensi.api.dto.PresensiDto;
import com.task.absensi.api.dto.StatusAbsenDto;
import com.task.absensi.api.model.Presensi;
import com.task.absensi.api.model.StatusAbsen;
import com.task.absensi.api.model.User;
import com.task.absensi.api.repo.PresensiRepository;
import com.task.absensi.api.repo.StatusAbsenRepository;
import com.task.absensi.common.ApiException;
import com.task.absensi.common.EpochSeconds;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class PresensiService {
    private final PresensiRepository presensiRepository;
    private final StatusAbsenRepository statusAbsenRepository;
    private final ZoneId zoneId;

    public PresensiService(PresensiRepository presensiRepository, StatusAbsenRepository statusAbsenRepository, ZoneId zoneId) {
        this.presensiRepository = presensiRepository;
        this.statusAbsenRepository = statusAbsenRepository;
        this.zoneId = zoneId;
    }

    public List<StatusAbsenDto> comboStatusAbsen(long tglAwal, long tglAkhir) {
        if (tglAwal > tglAkhir) {
            throw new ApiException("Range tanggal tidak valid.");
        }
        List<StatusAbsenDto> out = new ArrayList<StatusAbsenDto>();
        for (StatusAbsen s : statusAbsenRepository.findAll()) {
            out.add(new StatusAbsenDto(s.getKdStatus(), s.getNamaStatus()));
        }
        return out;
    }

    public List<PresensiDto> daftarAdmin(long tglAwal, long tglAkhir) {
        LocalDate start = EpochSeconds.toLocalDate(tglAwal, zoneId);
        LocalDate end = EpochSeconds.toLocalDate(tglAkhir, zoneId);
        if (start.isAfter(end)) {
            throw new ApiException("Range tanggal tidak valid.");
        }
        List<PresensiDto> out = new ArrayList<PresensiDto>();
        for (Presensi p : presensiRepository.findByTglAbsensiBetween(start, end)) {
            out.add(toDto(p));
        }
        return out;
    }

    public List<PresensiDto> daftarPegawai(User user, long tglAwal, long tglAkhir) {
        LocalDate start = EpochSeconds.toLocalDate(tglAwal, zoneId);
        LocalDate end = EpochSeconds.toLocalDate(tglAkhir, zoneId);
        if (start.isAfter(end)) {
            throw new ApiException("Range tanggal tidak valid.");
        }
        List<PresensiDto> out = new ArrayList<PresensiDto>();
        for (Presensi p : presensiRepository.findByUserAndTglAbsensiBetween(user, start, end)) {
            out.add(toDto(p));
        }
        return out;
    }

    public PresensiDto checkIn(User user) {
        LocalDate today = LocalDate.now(zoneId);
        LocalTime now = LocalTime.now(zoneId);
        Presensi presensi = presensiRepository.findByUserAndTglAbsensi(user, today).orElseGet(Presensi::new);

        if (presensi.getId() == null) {
            presensi.setUser(user);
            presensi.setTglAbsensi(today);
        }

        if (presensi.getJamMasuk() != null) {
            throw new ApiException("Anda sudah check-in hari ini.");
        }

        presensi.setJamMasuk(now.withNano(0));
        if (presensi.getStatusAbsen() == null) {
            StatusAbsen hadir = statusAbsenRepository.findByKdStatus("H").orElse(null);
            presensi.setStatusAbsen(hadir);
        }

        presensiRepository.save(presensi);
        return toDto(presensi);
    }

    public PresensiDto checkOut(User user) {
        LocalDate today = LocalDate.now(zoneId);
        LocalTime now = LocalTime.now(zoneId);
        Presensi presensi = presensiRepository.findByUserAndTglAbsensi(user, today)
                .orElseThrow(() -> new ApiException("Anda belum check-in hari ini."));

        if (presensi.getJamMasuk() == null) {
            throw new ApiException("Anda belum check-in hari ini.");
        }
        if (presensi.getJamKeluar() != null) {
            throw new ApiException("Anda sudah check-out hari ini.");
        }

        presensi.setJamKeluar(now.withNano(0));
        presensiRepository.save(presensi);
        return toDto(presensi);
    }

    public PresensiDto abseni(User user, AbseniRequest request) {
        LocalDate tgl = EpochSeconds.toLocalDate(request.getTglAbsensi(), zoneId);
        StatusAbsen status = statusAbsenRepository.findByKdStatus(request.getKdStatus())
                .orElseThrow(() -> new ApiException("Status absen tidak ditemukan."));

        Presensi presensi = presensiRepository.findByUserAndTglAbsensi(user, tgl).orElseGet(Presensi::new);
        if (presensi.getId() == null) {
            presensi.setUser(user);
            presensi.setTglAbsensi(tgl);
        }

        presensi.setStatusAbsen(status);
        presensi.setJamMasuk(null);
        presensi.setJamKeluar(null);
        presensiRepository.save(presensi);
        return toDto(presensi);
    }

    private PresensiDto toDto(Presensi presensi) {
        PresensiDto dto = new PresensiDto();
        dto.setIdUser(presensi.getUser().getId());
        dto.setNamaLengkap(presensi.getUser().getNamaLengkap());
        dto.setTglAbsensi(EpochSeconds.fromLocalDate(presensi.getTglAbsensi(), zoneId));
        dto.setJamMasuk(presensi.getJamMasuk() != null ? presensi.getJamMasuk().toString() : null);
        dto.setJamKeluar(presensi.getJamKeluar() != null ? presensi.getJamKeluar().toString() : null);
        if (presensi.getStatusAbsen() != null) {
            dto.setNamaStatus(presensi.getStatusAbsen().getNamaStatus());
        } else if (presensi.getJamMasuk() != null) {
            dto.setNamaStatus("Hadir");
        }
        return dto;
    }
}
