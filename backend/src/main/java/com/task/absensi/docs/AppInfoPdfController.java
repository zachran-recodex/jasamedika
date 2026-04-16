package com.task.absensi.docs;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
public class AppInfoPdfController {

    @GetMapping("/docs/app-info.pdf")
    public ResponseEntity<byte[]> appInfoPdf() throws Exception {
        byte[] pdf = buildPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=app-info.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private byte[] buildPdf() throws Exception {
        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(document, page);
            try {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(50, 740);
                cs.showText("Dokumen Informasi Aplikasi - Absensi");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(50, 710);
                writeLine(cs, "Backend: Java Spring Boot");
                writeLine(cs, "Auth: JWT Bearer Token");
                writeLine(cs, "Database: H2 (in-memory) untuk development");
                writeLine(cs, "Format tanggal: Epoch detik (bukan milidetik)");
                writeLine(cs, "");
                writeLine(cs, "Cara menjalankan (Windows):");
                writeLine(cs, "1) Masuk folder backend");
                writeLine(cs, "2) Jalankan: .\\\\mvnw.cmd spring-boot:run");
                writeLine(cs, "3) UI web: http://localhost:8080/");
                writeLine(cs, "");
                writeLine(cs, "Endpoint utama:");
                writeLine(cs, "- POST /api/auth/init-data");
                writeLine(cs, "- POST /api/auth/login");
                writeLine(cs, "- GET /api/pegawai/daftar (Admin/HRD)");
                writeLine(cs, "- GET /presensi/in, /presensi/out");
                writeLine(cs, "");
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                writeLine(cs, "Generated: " + ZonedDateTime.now(zoneId).toString());
                cs.endText();
            } finally {
                cs.close();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } finally {
            document.close();
        }
    }

    private void writeLine(PDPageContentStream cs, String text) throws Exception {
        cs.showText(text);
        cs.newLineAtOffset(0, -16);
    }
}

