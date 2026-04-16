param(
  [string]$BackendDir = (Join-Path $PSScriptRoot "..\\backend"),
  [string]$OutDir = (Join-Path $PSScriptRoot "..\\deliverables"),
  [string]$BaseUrl = "http://localhost:8080",
  [string]$SevenZipPath = "C:\\Program Files\\7-Zip\\7z.exe"
)

$ErrorActionPreference = "Stop"

New-Item -ItemType Directory -Force -Path $OutDir | Out-Null
New-Item -ItemType Directory -Force -Path (Join-Path $OutDir "pdf") | Out-Null
New-Item -ItemType Directory -Force -Path (Join-Path $OutDir "screenshot") | Out-Null

$pdfUrl = "$BaseUrl/docs/app-info.pdf"
$pdfOut = Join-Path $OutDir "pdf\\app-info.pdf"
Invoke-WebRequest -UseBasicParsing -Uri $pdfUrl -OutFile $pdfOut

$srcOut = Join-Path $OutDir "source"
if (Test-Path $srcOut) { Remove-Item -Recurse -Force $srcOut }
Copy-Item -Recurse -Force (Join-Path $PSScriptRoot "..\\backend") $srcOut
Copy-Item -Recurse -Force (Join-Path $PSScriptRoot "..\\desktop-app") (Join-Path $OutDir "desktop-app")

$archiveOut = Join-Path $OutDir "absensi-deliverable.7z"
if (Test-Path $archiveOut) { Remove-Item -Force $archiveOut }

if (Test-Path $SevenZipPath) {
  & $SevenZipPath a -t7z $archiveOut (Join-Path $OutDir "*") | Out-Null
  Write-Host "OK: $archiveOut"
} else {
  Write-Host "7z tidak ditemukan di $SevenZipPath. Silakan install 7-Zip atau ubah parameter -SevenZipPath."
  Write-Host "Folder deliverables siap di: $OutDir"
}

