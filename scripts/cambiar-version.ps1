# Script para cambiar de versión o rama en PowerShell
param (
    [string]$Version = ""
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Gym TobyFit - Gestor de Versiones" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Mostrar versiones/tags disponibles
Write-Host "`nVersiones disponibles en el repositorio:" -ForegroundColor Yellow
git tag -l

if (-not $Version) {
    Write-Host "`nEscribe el nombre de la versión (ej. v1.0.0) o 'main' para volver a la última versión:" -ForegroundColor Green
    $Version = Read-Host "Versión o rama"
}

if ($Version -eq "main") {
    Write-Host "`nVolviendo a la rama principal (main)..." -ForegroundColor Cyan
    git checkout main
} elseif ($Version) {
    Write-Host "`nCambiando a la versión $Version..." -ForegroundColor Cyan
    git checkout $Version
}

Write-Host "`nEstado actual:" -ForegroundColor Green
git status -s
Write-Host "`nRama o versión activa:" -ForegroundColor Green
git branch --show-current
git describe --tags --always
