package com.task.absensi.auth;

import com.task.absensi.api.dto.InitDataRequest;
import com.task.absensi.api.dto.InitDataResponse;
import com.task.absensi.api.dto.LoginRequest;
import com.task.absensi.api.dto.LoginResponse;
import com.task.absensi.api.dto.UbahPasswordRequest;
import com.task.absensi.api.security.AppUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/init-data")
    public InitDataResponse initData(@Valid @RequestBody InitDataRequest request) {
        return authService.initData(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/ubah-password-sendiri")
    public ResponseEntity<Map<String, Object>> ubahPasswordSendiri(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody UbahPasswordRequest request
    ) {
        authService.ubahPasswordSendiri(userDetails.getUser(), request);
        Map<String, Object> body = new LinkedHashMap<String, Object>();
        body.put("message", "Password berhasil diubah.");
        return ResponseEntity.ok(body);
    }
}
