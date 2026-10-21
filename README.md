# Open In Codex

[中文版说明](README.zh-CN.md)

IntelliJ IDEA plugin for opening the current project or current file location in a new Codex desktop session.

## Changes in this fork

Forked from [Like-jian/idea-open-in-codex](https://github.com/Like-jian/idea-open-in-codex).

`Open File In Codex` now leaves a blank input line after the file path and line range, with one ordinary space already filled in. You can immediately type your own prompt on that line, without inserting a line break yourself. No `Prompt:` label is inserted.

The file action appends two line feeds and one ordinary space (`"\n\n "`) to the existing context. A single trailing line feed is removed by the Codex composer; the additional line feed and space preserve a final input paragraph.

- Verified on Windows with Rider 2026.2.1 and Codex desktop 26.1013.4894.0, including an actual check in the composer.
- `Copy File Prompt` and `Open Project In Codex` retain their original behavior.
- `Open File In Codex` still opens a new Codex desktop session.

## Actions

- `Open File In Codex`: opens `codex://new` with the current project path, file path, selected line range or caret line, and a final blank input line.
- `Copy File Prompt`: copies the same current file and line range prompt to the clipboard without opening Codex. It is available from the editor and project view context menus.
- `Open Project In Codex`: opens `codex://new` with the current project path and a prompt containing the project path.

Default shortcuts:

- `Alt+Shift+O`: open current file in Codex
- `Alt+Shift+P`: open current project in Codex

## Rider Visual Assist shortcut conflict

Rider's **Visual Assist** keymap already assigns `Alt+Shift+O` to **Go to File** (`GotoFile`). A custom keymap inheriting from Visual Assist also inherits that binding, so the shortcut can open the file-search window instead of Codex.

If you already use a custom keymap, close Rider and back up its XML file in `%APPDATA%\JetBrains\Rider<version>\keymaps\`. Add these actions inside its existing `<keymap>` element, keeping the scheme name, parent, and other actions. If either action already exists, update that action instead of adding a duplicate:

```xml
<action id="GotoFile" />
<action id="OpenFileInCodex">
  <keyboard-shortcut first-keystroke="alt shift O" />
</action>
```

The empty `GotoFile` action clears its inherited shortcut. If you assigned additional shortcuts to `GotoFile`, keep those entries and remove only `Alt+Shift+O` instead of clearing the action. Also preserve any other shortcuts already assigned to `OpenFileInCodex`. Restart Rider and press `Alt+Shift+O` while the code editor is focused. This fix was verified on Windows with Rider 2026.2.1.

For a fresh Visual Assist setup, an optional [Visual Assist - Codex preset](<keymaps/Visual Assist - Codex.xml>) is included:

1. Install Open In Codex and enable the Visual Assist Keymap plugin.
2. Close Rider and copy the preset into `%APPDATA%\JetBrains\Rider<version>\keymaps\`.
3. Start Rider and select **Visual Assist - Codex** under **Settings | Keymap**.

The preset is enabled manually. It inherits the other Visual Assist shortcuts and assigns `Alt+Shift+O` to `Open File In Codex`. Keep your existing custom scheme and use the XML adjustment above if you want to retain personal shortcut changes.

## Local Build

This repository includes a PowerShell build script that compiles against the locally installed IntelliJ IDEA:

```powershell
.\scripts\build-plugin.ps1
```

The installable plugin archive is generated at:

```text
build\distributions\idea-open-in-codex-1.1.2.zip
```

Install it from IntelliJ IDEA with `Settings | Plugins | Install Plugin from Disk...`.
