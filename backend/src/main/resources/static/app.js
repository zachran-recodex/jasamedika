function setOutput(value) {
  const el = document.getElementById("output");
  el.textContent = typeof value === "string" ? value : JSON.stringify(value, null, 2);
}

function setLoginInfo(value) {
  document.getElementById("loginInfo").textContent = value || "";
}

function getToken() {
  return localStorage.getItem("token") || "";
}

function setToken(token) {
  if (token) {
    localStorage.setItem("token", token);
  } else {
    localStorage.removeItem("token");
  }
}

async function api(path, { method = "GET", body } = {}) {
  const headers = { "Content-Type": "application/json" };
  const token = getToken();
  if (token) headers.Authorization = `Bearer ${token}`;

  const res = await fetch(path, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  const text = await res.text();
  let json;
  try {
    json = text ? JSON.parse(text) : null;
  } catch (e) {
    json = { raw: text };
  }
  if (!res.ok) {
    throw { status: res.status, body: json };
  }
  return json;
}

function toEpochSecondsJakarta(dateStr) {
  if (!dateStr) return null;
  const [y, m, d] = dateStr.split("-").map((x) => parseInt(x, 10));
  const utcMidnightMs = Date.UTC(y, m - 1, d, 0, 0, 0);
  const jakartaOffsetMs = 7 * 60 * 60 * 1000;
  return Math.floor((utcMidnightMs - jakartaOffsetMs) / 1000);
}

document.getElementById("btnInitData").addEventListener("click", async () => {
  try {
    const namaAdmin = document.getElementById("initNamaAdmin").value.trim();
    const perusahaan = document.getElementById("initPerusahaan").value.trim();
    const out = await api("/api/auth/init-data", { method: "POST", body: { namaAdmin, perusahaan } });
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnLogin").addEventListener("click", async () => {
  try {
    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value;
    const out = await api("/api/auth/login", { method: "POST", body: { username, password } });
    setToken(out.token);
    setLoginInfo(`Login OK. Token tersimpan. ID User: ${out.info?.idUser ?? "-"}`);
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnLogout").addEventListener("click", () => {
  setToken("");
  setLoginInfo("Logout OK.");
});

document.getElementById("btnDaftarPegawai").addEventListener("click", async () => {
  try {
    const out = await api("/api/pegawai/daftar");
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnTambahPegawai").addEventListener("click", async () => {
  try {
    const username = document.getElementById("tambahUsername").value.trim();
    const namaLengkap = document.getElementById("tambahNama").value.trim();
    const out = await api("/pegawai/admin-tambah-pegawai", { method: "POST", body: { username, namaLengkap } });
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnPresensiIn").addEventListener("click", async () => {
  try {
    const out = await api("/presensi/in");
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnPresensiOut").addEventListener("click", async () => {
  try {
    const out = await api("/presensi/out");
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnDaftarPresensiSaya").addEventListener("click", async () => {
  try {
    const tglAwal = toEpochSecondsJakarta(document.getElementById("tglAwal").value);
    const tglAkhir = toEpochSecondsJakarta(document.getElementById("tglAkhir").value);
    const params = new URLSearchParams({ tglAwal: String(tglAwal), tglAkhir: String(tglAkhir) });
    const out = await api(`/presensi/daftar/pegawai?${params.toString()}`);
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

document.getElementById("btnDaftarPresensiAdmin").addEventListener("click", async () => {
  try {
    const tglAwal = toEpochSecondsJakarta(document.getElementById("tglAwal").value);
    const tglAkhir = toEpochSecondsJakarta(document.getElementById("tglAkhir").value);
    const params = new URLSearchParams({ tglAwal: String(tglAwal), tglAkhir: String(tglAkhir) });
    const out = await api(`/presensi/daftar/admin?${params.toString()}`);
    setOutput(out);
  } catch (e) {
    setOutput(e);
  }
});

if (getToken()) {
  setLoginInfo("Token ditemukan dari localStorage.");
}

