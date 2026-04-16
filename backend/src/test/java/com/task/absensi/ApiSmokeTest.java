package com.task.absensi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.absensi.common.EpochSeconds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiSmokeTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void fullFlow_smoke() throws Exception {
        String initBody = "{\"namaAdmin\":\"Admin Satu\",\"perusahaan\":\"PT Contoh\"}";
        mockMvc.perform(post("/api/auth/init-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initBody))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode loginJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String adminToken = loginJson.get("token").asText();
        assertThat(adminToken).isNotBlank();

        mockMvc.perform(get("/api/pegawai/combo/jabatan")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        String tambahPegawaiBody = "{\"username\":\"user1\",\"namaLengkap\":\"Pegawai 1\"}";
        MvcResult tambahPegawaiResult = mockMvc.perform(post("/pegawai/admin-tambah-pegawai")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tambahPegawaiBody))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode tambahPegawaiJson = objectMapper.readTree(tambahPegawaiResult.getResponse().getContentAsString());
        String userPassword = tambahPegawaiJson.get("password").asText();
        assertThat(userPassword).isNotBlank();

        MvcResult userLoginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user1\",\"password\":\"" + userPassword + "\"}"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode userLoginJson = objectMapper.readTree(userLoginResult.getResponse().getContentAsString());
        String userToken = userLoginJson.get("token").asText();
        assertThat(userToken).isNotBlank();

        MvcResult presensiInResult = mockMvc.perform(get("/presensi/in")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode presensiInJson = objectMapper.readTree(presensiInResult.getResponse().getContentAsString());
        long tglAbsensi = presensiInJson.get("tglAbsensi").asLong();

        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        long expected = EpochSeconds.fromLocalDate(LocalDate.now(zoneId), zoneId);
        assertThat(tglAbsensi).isEqualTo(expected);
    }
}

