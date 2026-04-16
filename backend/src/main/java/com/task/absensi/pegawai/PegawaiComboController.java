package com.task.absensi.pegawai;

import com.task.absensi.api.dto.ReferenceDto;
import com.task.absensi.api.dto.UserProfileDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pegawai/combo")
public class PegawaiComboController {
    private final PegawaiService pegawaiService;

    public PegawaiComboController(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

    @GetMapping("/jabatan")
    public List<ReferenceDto> jabatan() {
        return pegawaiService.comboJabatan();
    }

    @GetMapping("/departemen")
    public List<ReferenceDto> departemen() {
        return pegawaiService.comboDepartemen();
    }

    @GetMapping("/unit-kerja")
    public List<ReferenceDto> unitKerja() {
        return pegawaiService.comboUnitKerja();
    }

    @GetMapping("/pendidikan")
    public List<ReferenceDto> pendidikan() {
        return pegawaiService.comboPendidikan();
    }

    @GetMapping("/jenis-kelamin")
    public List<ReferenceDto> jenisKelamin() {
        return pegawaiService.comboJenisKelamin();
    }

    @GetMapping("/departemen-hrd")
    public List<UserProfileDto> departemenHrd() {
        return pegawaiService.comboDepartemenHrd();
    }
}
