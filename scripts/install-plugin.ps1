param(
    [string]$IdeaPluginsDir = "$env:APPDATA\JetBrains\IntelliJIdea2026.1\plugins"
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$buildScript = Join-Path $PSScriptRoot "build-plugin.ps1"
$pluginName = "idea-open-in-codex"
$version = "0.1.0"
$sourceJar = Join-Path $root "build\distributions\idea-open-in-codex.jar"
$targetLibDir = Join-Path $IdeaPluginsDir "$pluginName\lib"
$targetJar = Join-Path $targetLibDir "$pluginName-$version.jar"

& powershell.exe -NoProfile -ExecutionPolicy Bypass -File $buildScript
if ($LASTEXITCODE -ne 0) {
    throw "build-plugin.ps1 failed with exit code $LASTEXITCODE"
}

if (!(Test-Path $sourceJar)) {
    throw "Cannot find built plugin jar: $sourceJar"
}

New-Item $targetLibDir -ItemType Directory -Force | Out-Null
Copy-Item $sourceJar $targetJar -Force

Write-Host "Installed plugin files:"
Write-Host (Join-Path $IdeaPluginsDir $pluginName)
Write-Host "Restart IntelliJ IDEA to load the plugin."
