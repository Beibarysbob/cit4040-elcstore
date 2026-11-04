$ErrorActionPreference = "Stop"
if (Test-Path .env) { Write-Host ".env already exists. Keeping it."; exit }
$bytes1 = New-Object byte[] 24
$bytes2 = New-Object byte[] 48
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes1)
$rng.GetBytes($bytes2)
$db = [System.BitConverter]::ToString($bytes1).Replace("-", "")
$jwt = [System.BitConverter]::ToString($bytes2).Replace("-", "")
[System.IO.File]::WriteAllText((Join-Path (Get-Location) ".env"), "DB_PASSWORD=$db`nJWT_SECRET=$jwt", [System.Text.Encoding]::ASCII)
Write-Host "Created local .env with random credentials (never upload it)."
