package com.task.absensidesktop;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

public class DesktopApp {
    private final Gson gson = new Gson();
    private final ZoneId zoneId = ZoneId.of("Asia/Jakarta");

    private final ApiClient apiClient = new ApiClient("http://localhost:8080");

    private JFrame frame;
    private JTextField baseUrlField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField namaAdminField;
    private JTextField perusahaanField;
    private JTextField tglAwalField;
    private JTextField tglAkhirField;
    private JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DesktopApp().start();
            }
        });
    }

    private void start() {
        frame = new JFrame("Absensi Desktop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 720);

        JPanel topPanel = buildTopPanel();
        JPanel actionPanel = buildActionPanel();
        JScrollPane outputPanel = buildOutputPanel();

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(actionPanel, BorderLayout.CENTER);
        frame.getContentPane().add(outputPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));

        panel.add(new JLabel("Base URL"));
        baseUrlField = new JTextField(apiClient.getBaseUrl());
        panel.add(baseUrlField);
        JButton applyBaseUrl = new JButton("Set");
        applyBaseUrl.addActionListener(e -> {
            apiClient.setBaseUrl(baseUrlField.getText());
            info("Base URL diset: " + apiClient.getBaseUrl());
        });
        panel.add(applyBaseUrl);
        panel.add(new JLabel(""));

        panel.add(new JLabel("Nama Admin"));
        namaAdminField = new JTextField("Admin Satu");
        panel.add(namaAdminField);
        panel.add(new JLabel("Perusahaan"));
        perusahaanField = new JTextField("PT Contoh");
        panel.add(perusahaanField);

        panel.add(new JLabel("Username"));
        usernameField = new JTextField("admin");
        panel.add(usernameField);
        panel.add(new JLabel("Password"));
        passwordField = new JTextField("admin123");
        panel.add(passwordField);

        return panel;
    }

    private JPanel buildActionPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 3, 8, 8));

        JButton btnInitData = new JButton("POST /api/auth/init-data");
        btnInitData.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }));

        JButton btnLogin = new JButton("POST /api/auth/login");
        btnLogin.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                login();
            }
        }));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            apiClient.setToken(null);
            setOutput("{\"message\":\"logout\"}");
        });

        JButton btnPresensiIn = new JButton("GET /presensi/in");
        btnPresensiIn.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                getJson("/presensi/in");
            }
        }));

        JButton btnPresensiOut = new JButton("GET /presensi/out");
        btnPresensiOut.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                getJson("/presensi/out");
            }
        }));

        JButton btnDaftarPegawai = new JButton("GET /api/pegawai/daftar");
        btnDaftarPegawai.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                getJson("/api/pegawai/daftar");
            }
        }));

        panel.add(btnInitData);
        panel.add(btnLogin);
        panel.add(btnLogout);

        panel.add(btnPresensiIn);
        panel.add(btnPresensiOut);
        panel.add(btnDaftarPegawai);

        panel.add(new JLabel("Tgl Awal (YYYY-MM-DD)"));
        tglAwalField = new JTextField(LocalDate.now(zoneId).toString());
        panel.add(tglAwalField);
        JButton btnDaftarPresensiSaya = new JButton("GET /presensi/daftar/pegawai");
        btnDaftarPresensiSaya.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                daftarPresensiSaya();
            }
        }));
        panel.add(btnDaftarPresensiSaya);

        panel.add(new JLabel("Tgl Akhir (YYYY-MM-DD)"));
        tglAkhirField = new JTextField(LocalDate.now(zoneId).toString());
        panel.add(tglAkhirField);
        JButton btnDaftarPresensiAdmin = new JButton("GET /presensi/daftar/admin");
        btnDaftarPresensiAdmin.addActionListener(e -> runAsync(new Runnable() {
            @Override
            public void run() {
                daftarPresensiAdmin();
            }
        }));
        panel.add(btnDaftarPresensiAdmin);

        return panel;
    }

    private JScrollPane buildOutputPanel() {
        outputArea = new JTextArea();
        outputArea.setRows(14);
        outputArea.setEditable(false);
        return new JScrollPane(outputArea);
    }

    private void initData() {
        JsonObject body = new JsonObject();
        body.addProperty("namaAdmin", namaAdminField.getText());
        body.addProperty("perusahaan", perusahaanField.getText());
        try {
            JsonElement out = apiClient.post("/api/auth/init-data", body);
            setOutput(gson.toJson(out));
        } catch (IOException ex) {
            setOutput(errorJson(ex));
        }
    }

    private void login() {
        JsonObject body = new JsonObject();
        body.addProperty("username", usernameField.getText());
        body.addProperty("password", passwordField.getText());
        try {
            JsonElement out = apiClient.post("/api/auth/login", body);
            String token = out.getAsJsonObject().get("token").getAsString();
            apiClient.setToken(token);
            setOutput(gson.toJson(out));
        } catch (Exception ex) {
            setOutput(errorJson(ex));
        }
    }

    private void daftarPresensiSaya() {
        long tglAwal = EpochUtil.localDateToEpochSeconds(LocalDate.parse(tglAwalField.getText()), zoneId);
        long tglAkhir = EpochUtil.localDateToEpochSeconds(LocalDate.parse(tglAkhirField.getText()), zoneId);
        getJson("/presensi/daftar/pegawai?tglAwal=" + tglAwal + "&tglAkhir=" + tglAkhir);
    }

    private void daftarPresensiAdmin() {
        long tglAwal = EpochUtil.localDateToEpochSeconds(LocalDate.parse(tglAwalField.getText()), zoneId);
        long tglAkhir = EpochUtil.localDateToEpochSeconds(LocalDate.parse(tglAkhirField.getText()), zoneId);
        getJson("/presensi/daftar/admin?tglAwal=" + tglAwal + "&tglAkhir=" + tglAkhir);
    }

    private void getJson(String path) {
        try {
            JsonElement out = apiClient.get(path);
            setOutput(gson.toJson(out));
        } catch (IOException ex) {
            setOutput(errorJson(ex));
        }
    }

    private void setOutput(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outputArea.setText(text);
            }
        });
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private String errorJson(Exception ex) {
        JsonObject obj = new JsonObject();
        obj.addProperty("error", ex.getMessage());
        return gson.toJson(obj);
    }

    private void runAsync(final Runnable action) {
        new Thread(action).start();
    }
}

