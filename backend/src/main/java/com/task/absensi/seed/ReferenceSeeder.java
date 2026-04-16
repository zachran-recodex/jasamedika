package com.task.absensi.seed;

import com.task.absensi.api.model.Departemen;
import com.task.absensi.api.model.Jabatan;
import com.task.absensi.api.model.JenisKelamin;
import com.task.absensi.api.model.Pendidikan;
import com.task.absensi.api.model.StatusAbsen;
import com.task.absensi.api.model.UnitKerja;
import com.task.absensi.api.repo.DepartemenRepository;
import com.task.absensi.api.repo.JabatanRepository;
import com.task.absensi.api.repo.JenisKelaminRepository;
import com.task.absensi.api.repo.PendidikanRepository;
import com.task.absensi.api.repo.StatusAbsenRepository;
import com.task.absensi.api.repo.UnitKerjaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ReferenceSeeder implements CommandLineRunner {
    private final JabatanRepository jabatanRepository;
    private final DepartemenRepository departemenRepository;
    private final UnitKerjaRepository unitKerjaRepository;
    private final PendidikanRepository pendidikanRepository;
    private final JenisKelaminRepository jenisKelaminRepository;
    private final StatusAbsenRepository statusAbsenRepository;

    public ReferenceSeeder(
            JabatanRepository jabatanRepository,
            DepartemenRepository departemenRepository,
            UnitKerjaRepository unitKerjaRepository,
            PendidikanRepository pendidikanRepository,
            JenisKelaminRepository jenisKelaminRepository,
            StatusAbsenRepository statusAbsenRepository
    ) {
        this.jabatanRepository = jabatanRepository;
        this.departemenRepository = departemenRepository;
        this.unitKerjaRepository = unitKerjaRepository;
        this.pendidikanRepository = pendidikanRepository;
        this.jenisKelaminRepository = jenisKelaminRepository;
        this.statusAbsenRepository = statusAbsenRepository;
    }

    @Override
    public void run(String... args) {
        seedNamedReferences();
        seedStatusAbsen();
    }

    private void seedNamedReferences() {
        if (jabatanRepository.count() == 0) {
            saveAllJabatan(Arrays.asList("Staff", "Supervisor", "Manager", "Direktur"));
        }
        if (departemenRepository.count() == 0) {
            saveAllDepartemen(Arrays.asList("HRD", "IT", "Finance", "Operasional"));
        }
        if (unitKerjaRepository.count() == 0) {
            saveAllUnitKerja(Arrays.asList("Kantor Pusat", "Cabang A", "Cabang B"));
        }
        if (pendidikanRepository.count() == 0) {
            saveAllPendidikan(Arrays.asList("SMA/SMK", "D3", "S1", "S2"));
        }
        if (jenisKelaminRepository.count() == 0) {
            saveAllJenisKelamin(Arrays.asList("Laki-laki", "Perempuan"));
        }
    }

    private void seedStatusAbsen() {
        if (statusAbsenRepository.count() > 0) {
            return;
        }

        StatusAbsen hadir = new StatusAbsen();
        hadir.setKdStatus("H");
        hadir.setNamaStatus("Hadir");

        StatusAbsen izin = new StatusAbsen();
        izin.setKdStatus("I");
        izin.setNamaStatus("Izin");

        StatusAbsen sakit = new StatusAbsen();
        sakit.setKdStatus("S");
        sakit.setNamaStatus("Sakit");

        StatusAbsen alpa = new StatusAbsen();
        alpa.setKdStatus("A");
        alpa.setNamaStatus("Alpa");

        statusAbsenRepository.saveAll(Arrays.asList(hadir, izin, sakit, alpa));
    }

    private void saveAllJabatan(List<String> names) {
        for (String name : names) {
            Jabatan j = new Jabatan();
            j.setNama(name);
            jabatanRepository.save(j);
        }
    }

    private void saveAllDepartemen(List<String> names) {
        for (String name : names) {
            Departemen d = new Departemen();
            d.setNama(name);
            departemenRepository.save(d);
        }
    }

    private void saveAllUnitKerja(List<String> names) {
        for (String name : names) {
            UnitKerja u = new UnitKerja();
            u.setNama(name);
            unitKerjaRepository.save(u);
        }
    }

    private void saveAllPendidikan(List<String> names) {
        for (String name : names) {
            Pendidikan p = new Pendidikan();
            p.setNama(name);
            pendidikanRepository.save(p);
        }
    }

    private void saveAllJenisKelamin(List<String> names) {
        for (String name : names) {
            JenisKelamin j = new JenisKelamin();
            j.setNama(name);
            jenisKelaminRepository.save(j);
        }
    }
}
