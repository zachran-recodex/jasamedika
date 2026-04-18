package com.task.absensi.common;

import com.task.absensi.api.dto.UserProfileDto;
import com.task.absensi.api.model.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class UserProfileMapper {
    private final ZoneId zoneId;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public UserProfileMapper(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public UserProfileDto toDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setIdUser(user.getId());
        dto.setNamaLengkap(user.getNamaLengkap());
        dto.setTempatLahir(user.getTempatLahir());
        
        if (user.getTglLahir() != null) {
            long epochSeconds = EpochSeconds.fromLocalDate(user.getTglLahir(), zoneId);
            dto.setTglLahir(epochSeconds);
            
            // Format TTL as "Tempat, DD MMMM YYYY" format
            String formattedDate = user.getTglLahir().format(DATE_FORMATTER);
            if (user.getTempatLahir() != null && !user.getTempatLahir().isEmpty()) {
                dto.setTtl(user.getTempatLahir() + ", " + formattedDate);
            } else {
                dto.setTtl(formattedDate);
            }
        } else if (user.getTempatLahir() != null && !user.getTempatLahir().isEmpty()) {
            dto.setTtl(user.getTempatLahir());
        }

        dto.setEmail(user.getEmail());
        dto.setJabatan(user.getJabatan() != null ? user.getJabatan().getNama() : null);
        dto.setDepartemen(user.getDepartemen() != null ? user.getDepartemen().getNama() : null);
        dto.setUnitKerja(user.getUnitKerja() != null ? user.getUnitKerja().getNama() : null);
        dto.setJenisKelamin(user.getJenisKelamin() != null ? user.getJenisKelamin().getNama() : null);
        dto.setPendidikan(user.getPendidikan() != null ? user.getPendidikan().getNama() : null);
        
        if (user.getPhoto() != null && user.getPhoto().length > 0) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(user.getPhoto()));
        }
        
        return dto;
    }
}
