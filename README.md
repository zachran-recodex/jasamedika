# Absensi Fullstack (Java Spring Boot)

Project ini adalah implementasi spesifikasi API untuk tugas Jasamedika menggunakan Java Spring Boot, dengan UI web sederhana (tanpa framework) untuk kebutuhan Fullstack.

## Struktur Folder

- `backend/` : Spring Boot API
- `frontend/` : UI web statis yang dibangun sebagai asset dan disajikan oleh Spring Boot
- `desktop-app/` : aplikasi desktop Windows (Java Swing) untuk akses API (pengganti APK mobile)
- `scripts/` : skrip bantu untuk menyiapkan deliverables (PDF + source + archive)

## Prasyarat

- Java 8 (JRE sudah cukup untuk build di repo ini, karena compile menggunakan Eclipse JDT compiler lewat Maven)

## Menjalankan Aplikasi

Dari folder `backend`:

```bash
.\mvnw.cmd spring-boot:run
```

Lalu buka:

- UI Web: `http://localhost:8080/`
- H2 Console: `http://localhost:8080/h2`
- PDF info aplikasi: `http://localhost:8080/docs/app-info.pdf`

## Menjalankan Test

```bash
.\mvnw.cmd test
```

## Aplikasi Desktop (Windows)

Jika “APK” yang dimaksud adalah aplikasi desktop Windows, project ini menyediakan client desktop berbasis Java Swing.

Build dari folder `desktop-app`:

```bash
.\mvnw.cmd package
```

Hasilnya:

- `desktop-app/target/absensi-desktop.jar`

Jalankan:

```bash
java -jar .\target\absensi-desktop.jar
```

## Aturan Penting (Sesuai Spesifikasi)

- Format tanggal pada API menggunakan **Epoch detik** (bukan milidetik).
- Error bisnis/API dikembalikan sebagai **HTTP 501** dengan pesan yang mudah dimengerti.
- Error lain seperti 400/404/500 adalah error akses atau bug.
- API dirancang agar bisa dipanggil dari aplikasi web atau client lain.
- Request ke endpoint yang tidak ada akan kembali dengan HTTP 404.


## Alur Cepat (Minimal)

1) Init data (buat perusahaan + admin)

Endpoint:

- `POST /api/auth/init-data`

Body:

```json
{
  "namaAdmin": "Admin Satu",
  "perusahaan": "PT Contoh"
}
```

Default akun admin yang dibuat:

- `username`: `admin`
- `password`: `admin123`

2) Login untuk dapat token

- `POST /api/auth/login`

Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response:

```json
{
  "token": "BearerToken...",
  "info": {
    "idUser": 1,
    "namaLengkap": "Admin Satu",
    "Photo": "base64..."
  }
}
```

3) Gunakan token pada request selanjutnya

Header:

`Authorization: Bearer <token>`

## Endpoint (Ringkas)

### AUTH

- `POST /api/auth/init-data`
- `POST /api/auth/login`
- `POST /api/auth/ubah-password-sendiri`

### PEGAWAI (Combo)

- `GET /api/pegawai/combo/jabatan`
- `GET /api/pegawai/combo/departemen`
- `GET /api/pegawai/combo/unit-kerja`
- `GET /api/pegawai/combo/pendidikan`
- `GET /api/pegawai/combo/jenis-kelamin`
- `GET /api/pegawai/combo/departemen-hrd`

### Manajemen PEGAWAI

- `GET /api/pegawai/daftar` (Admin/HRD)
- `POST /api/pegawai/admin-tambah-pegawai` (Admin/HRD)
- `POST /api/pegawai/admin-ubah-pegawai` (Admin/HRD)
- `POST /api/pegawai/admin-ubah-photo?idUser=...` (Admin/HRD)
- `POST /api/pegawai/ubah-photo` (Pegawai, ubah foto sendiri)

### PRESENSI & ABSENSI

- `GET /api/presensi/combo/status-absen?tglAwal=EPOCH_SEC&tglAkhir=EPOCH_SEC`
- `GET /api/presensi/daftar/admin?tglAwal=EPOCH_SEC&tglAkhir=EPOCH_SEC` (Admin/HRD)
- `GET /api/presensi/daftar/pegawai?tglAwal=EPOCH_SEC&tglAkhir=EPOCH_SEC`
- `GET /api/presensi/in`
- `GET /api/presensi/out`
- `POST /api/presensi/abseni`

## UI Web

UI web berada di folder `frontend/` dan otomatis dimasukkan ke package Spring Boot saat build.

- Halaman ini menyimpan token di `localStorage` setelah login.
- Input tanggal pada halaman presensi akan dikonversi ke epoch detik (timezone Asia/Jakarta).

## Menyiapkan Deliverables (PDF + Source + Arsip)

Pastikan backend sudah running (agar PDF bisa diunduh), lalu jalankan:

```powershell
.\scripts\prepare-deliverables.ps1 -BaseUrl http://localhost:8080
```

Output akan dibuat di folder `deliverables/`. Jika 7-Zip terinstall di `C:\Program Files\7-Zip\7z.exe`, skrip akan membuat `deliverables/absensi-deliverable.7z`.
