param(
    [string]$IdeaHome = "D:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1"
)

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$buildDir = Join-Path $root "build"
$classesDir = Join-Path $buildDir "classes"
$jarStageDir = Join-Path $buildDir "jar-stage"
$distStageDir = Join-Path $buildDir "dist-stage"
$distDir = Join-Path $buildDir "distributions"
$pluginName = "idea-open-in-codex"
$version = "1.1.2"

function New-ZipFromDirectory {
    param(
        [string]$SourceDirectory,
        [string]$ZipPath
    )

    Add-Type -AssemblyName System.IO.Compression
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $sourceRoot = (Resolve-Path $SourceDirectory).Path
    $zip = [System.IO.Compression.ZipFile]::Open($ZipPath, [System.IO.Compression.ZipArchiveMode]::Create)
    try {
        Get-ChildItem $sourceRoot -Recurse -File | ForEach-Object {
            $relativePath = $_.FullName.Substring($sourceRoot.Length + 1).Replace('\', '/')
            [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, $_.FullName, $relativePath) | Out-Null
        }
    } finally {
        $zip.Dispose()
    }
}

$javac = Join-Path $IdeaHome "jbr\bin\javac.exe"
$jarCommand = Get-Command "jar.exe" -ErrorAction SilentlyContinue
if (!(Test-Path $javac)) {
    throw "Cannot find javac: $javac"
}

Remove-Item $classesDir, $jarStageDir, $distStageDir -Recurse -Force -ErrorAction SilentlyContinue
New-Item $classesDir, $jarStageDir, $distDir -ItemType Directory -Force | Out-Null

$sources = Get-ChildItem (Join-Path $root "src\main\java") -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
& $javac --release 17 -encoding UTF-8 -classpath "$IdeaHome\lib\*" -d $classesDir @sources
if ($LASTEXITCODE -ne 0) {
    throw "javac failed with exit code $LASTEXITCODE"
}

Copy-Item (Join-Path $classesDir "*") $jarStageDir -Recurse -Force
Copy-Item (Join-Path $root "src\main\resources\*") $jarStageDir -Recurse -Force

$jarPath = Join-Path $distDir "$pluginName.jar"
Remove-Item $jarPath -Force -ErrorAction SilentlyContinue
if ($jarCommand -ne $null) {
    Push-Location $jarStageDir
    try {
        & $jarCommand.Source cf $jarPath .
        if ($LASTEXITCODE -ne 0) {
            throw "jar failed with exit code $LASTEXITCODE"
        }
    } finally {
        Pop-Location
    }
} else {
    $tmpJarZip = Join-Path $distDir "$pluginName.jar.zip"
    Remove-Item $tmpJarZip -Force -ErrorAction SilentlyContinue
    Compress-Archive -Path (Join-Path $jarStageDir "*") -DestinationPath $tmpJarZip -Force
    Move-Item $tmpJarZip $jarPath -Force
}

$pluginLibDir = Join-Path $distStageDir "$pluginName\lib"
New-Item $pluginLibDir -ItemType Directory -Force | Out-Null
Copy-Item $jarPath (Join-Path $pluginLibDir "$pluginName-$version.jar") -Force

$zipPath = Join-Path $distDir "$pluginName-$version.zip"
Remove-Item $zipPath -Force -ErrorAction SilentlyContinue
New-ZipFromDirectory -SourceDirectory $distStageDir -ZipPath $zipPath

Write-Host "Built plugin:"
Write-Host $zipPath
