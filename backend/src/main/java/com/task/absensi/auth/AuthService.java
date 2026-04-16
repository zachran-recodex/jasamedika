package com.task.absensi.auth;

import com.task.absensi.api.dto.InitDataRequest;
import com.task.absensi.api.dto.InitDataResponse;
import com.task.absensi.api.dto.LoginRequest;
import com.task.absensi.api.dto.LoginResponse;
import com.task.absensi.api.dto.UbahPasswordRequest;
import com.task.absensi.api.model.Perusahaan;
import com.task.absensi.api.model.Role;
import com.task.absensi.api.model.User;
import com.task.absensi.api.repo.PerusahaanRepository;
import com.task.absensi.api.repo.UserRepository;
import com.task.absensi.api.security.JwtService;
import com.task.absensi.common.ApiException;
import com.task.absensi.common.UserProfileMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PerusahaanRepository perusahaanRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserProfileMapper userProfileMapper;

    public AuthService(
            PerusahaanRepository perusahaanRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserProfileMapper userProfileMapper
    ) {
        this.perusahaanRepository = perusahaanRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userProfileMapper = userProfileMapper;
    }

    public InitDataResponse initData(InitDataRequest request) {
        if (perusahaanRepository.count() > 0 || !userRepository.findByRole(Role.ADMIN).isEmpty()) {
            throw new ApiException("Data awal sudah dibuat, tidak bisa init ulang.");
        }

        Perusahaan perusahaan = new Perusahaan();
        perusahaan.setNama(request.getPerusahaan());
        perusahaanRepository.save(perusahaan);

        String username = "admin";
        String password = "admin123";

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ApiException("Username admin sudah terpakai, silakan reset database.");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setNamaLengkap(request.getNamaAdmin());
        userRepository.save(admin);

        InitDataResponse response = new InitDataResponse();
        response.setMessage("Data awal berhasil dibuat.");
        response.setUsername(username);
        response.setPassword(password);
        return response;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ApiException("Username atau password salah."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Username atau password salah.");
        }

        LoginResponse response = new LoginResponse();
        response.setToken(jwtService.generateToken(user));
        response.setInfo(userProfileMapper.toDto(user));
        return response;
    }

    public void ubahPasswordSendiri(User user, UbahPasswordRequest request) {
        if (!passwordEncoder.matches(request.getPasswordAsli(), user.getPasswordHash())) {
            throw new ApiException("Password asli tidak sesuai.");
        }
        if (!request.getPasswordBaru1().equals(request.getPasswordBaru2())) {
            throw new ApiException("Password baru tidak sama.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getPasswordBaru1()));
        userRepository.save(user);
    }
}
