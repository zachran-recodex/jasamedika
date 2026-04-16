package com.task.absensi.pegawai;

import com.task.absensi.api.dto.AdminTambahPegawaiRequest;
import com.task.absensi.api.dto.AdminUbahPegawaiRequest;
import com.task.absensi.api.dto.ReferenceDto;
import com.task.absensi.api.dto.TambahPegawaiResponse;
import com.task.absensi.api.dto.UbahPhotoRequest;
import com.task.absensi.api.dto.UserProfileDto;
import com.task.absensi.api.model.Departemen;
import com.task.absensi.api.model.Jabatan;
import com.task.absensi.api.model.JenisKelamin;
import com.task.absensi.api.model.Pendidikan;
import com.task.absensi.api.model.Role;
import com.task.absensi.api.model.UnitKerja;
import com.task.absensi.api.model.User;
import com.task.absensi.api.repo.DepartemenRepository;
import com.task.absensi.api.repo.JabatanRepository;
import com.task.absensi.api.repo.JenisKelaminRepository;
import com.task.absensi.api.repo.PendidikanRepository;
import com.task.absensi.api.repo.UnitKerjaRepository;
import com.task.absensi.api.repo.UserRepository;
import com.task.absensi.common.ApiException;
import com.task.absensi.common.EpochSeconds;
import com.task.absensi.common.UserProfileMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PegawaiService {
    private final JabatanRepository jabatanRepository;
    private final DepartemenRepository departemenRepository;
    private final UnitKerjaRepository unitKerjaRepository;
    private final PendidikanRepository pendidikanRepository;
    private final JenisKelaminRepository jenisKelaminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZoneId zoneId;
    private final UserProfileMapper userProfileMapper;

    public PegawaiService(
            JabatanRepository jabatanRepository,
            DepartemenRepository departemenRepository,
            UnitKerjaRepository unitKerjaRepository,
            PendidikanRepository pendidikanRepository,
            JenisKelaminRepository jenisKelaminRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ZoneId zoneId,
            UserProfileMapper userProfileMapper
    ) {
        this.jabatanRepository = jabatanRepository;
        this.departemenRepository = departemenRepository;
        this.unitKerjaRepository = unitKerjaRepository;
        this.pendidikanRepository = pendidikanRepository;
        this.jenisKelaminRepository = jenisKelaminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.zoneId = zoneId;
        this.userProfileMapper = userProfileMapper;
    }

    public List<ReferenceDto> comboJabatan() {
        List<ReferenceDto> out = new ArrayList<ReferenceDto>();
        for (Jabatan j : jabatanRepository.findAll()) {
            out.add(new ReferenceDto(j.getId(), j.getNama()));
        }
        return out;
    }

    public List<ReferenceDto> comboDepartemen() {
        List<ReferenceDto> out = new ArrayList<ReferenceDto>();
        for (Departemen d : departemenRepository.findAll()) {
            out.add(new ReferenceDto(d.getId(), d.getNama()));
        }
        return out;
    }

    public List<ReferenceDto> comboUnitKerja() {
        List<ReferenceDto> out = new ArrayList<ReferenceDto>();
        for (UnitKerja u : unitKerjaRepository.findAll()) {
            out.add(new ReferenceDto(u.getId(), u.getNama()));
        }
        return out;
    }

    public List<ReferenceDto> comboPendidikan() {
        List<ReferenceDto> out = new ArrayList<ReferenceDto>();
        for (Pendidikan p : pendidikanRepository.findAll()) {
            out.add(new ReferenceDto(p.getId(), p.getNama()));
        }
        return out;
    }

    public List<ReferenceDto> comboJenisKelamin() {
        List<ReferenceDto> out = new ArrayList<ReferenceDto>();
        for (JenisKelamin j : jenisKelaminRepository.findAll()) {
            out.add(new ReferenceDto(j.getId(), j.getNama()));
        }
        return out;
    }

    public List<UserProfileDto> comboDepartemenHrd() {
        Departemen hrd = departemenRepository.findByNama("HRD")
                .orElseThrow(() -> new ApiException("Departemen HRD belum tersedia."));
        List<UserProfileDto> out = new ArrayList<UserProfileDto>();
        for (User u : userRepository.findByDepartemen(hrd)) {
            out.add(userProfileMapper.toDto(u));
        }
        return out;
    }

    public List<UserProfileDto> daftarPegawai() {
        List<UserProfileDto> out = new ArrayList<UserProfileDto>();
        for (User user : userRepository.findAll()) {
            if (user.getRole() != Role.ADMIN) {
                out.add(userProfileMapper.toDto(user));
            }
        }
        return out;
    }

    public TambahPegawaiResponse adminTambahPegawai(AdminTambahPegawaiRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ApiException("Username sudah digunakan.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNamaLengkap(request.getNamaLengkap());
        user.setTempatLahir(request.getTempatLahir());
        if (request.getTglLahir() != null) {
            user.setTglLahir(EpochSeconds.toLocalDate(request.getTglLahir(), zoneId));
        }
        user.setEmail(request.getEmail());
        user.setJabatan(resolveJabatan(request.getIdJabatan()));
        user.setDepartemen(resolveDepartemen(request.getIdDepartemen()));
        user.setUnitKerja(resolveUnitKerja(request.getIdUnitKerja()));
        user.setJenisKelamin(resolveJenisKelamin(request.getIdJenisKelamin()));
        user.setPendidikan(resolvePendidikan(request.getIdPendidikan()));

        Role role = Role.PEGAWAI;
        if (user.getDepartemen() != null && "HRD".equalsIgnoreCase(user.getDepartemen().getNama())) {
            role = Role.HRD;
        }
        user.setRole(role);

        String tempPassword = "pegawai123";
        user.setPasswordHash(passwordEncoder.encode(tempPassword));

        userRepository.save(user);

        TambahPegawaiResponse response = new TambahPegawaiResponse();
        response.setMessage("Pegawai berhasil ditambahkan.");
        response.setPassword(tempPassword);
        response.setInfo(userProfileMapper.toDto(user));
        return response;
    }

    public UserProfileDto adminUbahPegawai(AdminUbahPegawaiRequest request) {
        User user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new ApiException("Pegawai tidak ditemukan."));

        if (user.getRole() == Role.ADMIN) {
            throw new ApiException("Tidak bisa mengubah data admin melalui endpoint ini.");
        }

        if (request.getNamaLengkap() != null) {
            user.setNamaLengkap(request.getNamaLengkap());
        }
        if (request.getTempatLahir() != null) {
            user.setTempatLahir(request.getTempatLahir());
        }
        if (request.getTglLahir() != null) {
            user.setTglLahir(EpochSeconds.toLocalDate(request.getTglLahir(), zoneId));
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getIdJabatan() != null) {
            user.setJabatan(resolveJabatan(request.getIdJabatan()));
        }
        if (request.getIdDepartemen() != null) {
            user.setDepartemen(resolveDepartemen(request.getIdDepartemen()));
        }
        if (request.getIdUnitKerja() != null) {
            user.setUnitKerja(resolveUnitKerja(request.getIdUnitKerja()));
        }
        if (request.getIdJenisKelamin() != null) {
            user.setJenisKelamin(resolveJenisKelamin(request.getIdJenisKelamin()));
        }
        if (request.getIdPendidikan() != null) {
            user.setPendidikan(resolvePendidikan(request.getIdPendidikan()));
        }

        if (user.getDepartemen() != null && "HRD".equalsIgnoreCase(user.getDepartemen().getNama())) {
            user.setRole(Role.HRD);
        } else {
            user.setRole(Role.PEGAWAI);
        }

        userRepository.save(user);
        return userProfileMapper.toDto(user);
    }

    public UserProfileDto adminUbahPhoto(Long idUser, UbahPhotoRequest request) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new ApiException("Pegawai tidak ditemukan."));
        applyPhoto(user, request);
        userRepository.save(user);
        return userProfileMapper.toDto(user);
    }

    public UserProfileDto ubahPhotoSendiri(User user, UbahPhotoRequest request) {
        applyPhoto(user, request);
        userRepository.save(user);
        return userProfileMapper.toDto(user);
    }

    private void applyPhoto(User user, UbahPhotoRequest request) {
        try {
            user.setPhoto(Base64.getDecoder().decode(request.getPhotoBase64()));
            user.setPhotoContentType(request.getContentType());
        } catch (Exception ex) {
            throw new ApiException("Format photo tidak valid.");
        }
    }

    private Jabatan resolveJabatan(Long id) {
        if (id == null) {
            return null;
        }
        return jabatanRepository.findById(id).orElseThrow(() -> new ApiException("Jabatan tidak ditemukan."));
    }

    private Departemen resolveDepartemen(Long id) {
        if (id == null) {
            return null;
        }
        return departemenRepository.findById(id).orElseThrow(() -> new ApiException("Departemen tidak ditemukan."));
    }

    private UnitKerja resolveUnitKerja(Long id) {
        if (id == null) {
            return null;
        }
        return unitKerjaRepository.findById(id).orElseThrow(() -> new ApiException("Unit kerja tidak ditemukan."));
    }

    private JenisKelamin resolveJenisKelamin(Long id) {
        if (id == null) {
            return null;
        }
        return jenisKelaminRepository.findById(id).orElseThrow(() -> new ApiException("Jenis kelamin tidak ditemukan."));
    }

    private Pendidikan resolvePendidikan(Long id) {
        if (id == null) {
            return null;
        }
        return pendidikanRepository.findById(id).orElseThrow(() -> new ApiException("Pendidikan tidak ditemukan."));
    }
}
