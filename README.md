# Open In Codex

[中文版说明](README.zh-CN.md)

IntelliJ IDEA plugin for opening the current project or current file location in a new Codex desktop session.

## Actions

- `Open File In Codex`: opens `codex://new` with the current project path and a Chinese prompt containing the current file plus selected line range or caret line.
- `Open Project In Codex`: opens `codex://new` with the current project path and a Chinese prompt containing the project path.

Default shortcuts:

- `Alt+Shift+O`: open current file in Codex
- `Alt+Shift+P`: open current project in Codex

## Local Build

This repository includes a PowerShell build script that compiles against the locally installed IntelliJ IDEA:

```powershell
.\scripts\build-plugin.ps1
```

The installable plugin archive is generated at:

```text
build\distributions\idea-open-in-codex-1.1.1.zip
```

Install it from IntelliJ IDEA with `Settings | Plugins | Install Plugin from Disk...`.
