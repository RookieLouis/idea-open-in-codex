# Open In Codex

这是一个 IntelliJ IDEA 插件，用于从 IDEA 中把当前项目或当前文件位置打开到新的 Codex 桌面端会话。

## 功能

- `Open File In Codex`：打开新的 Codex 会话，并带上当前项目路径、当前文件路径以及选中的行号范围；如果没有选中文本，则使用当前光标所在行。
- `Open Project In Codex`：打开新的 Codex 会话，并把当前项目路径作为 Codex 工作目录，同时附带当前项目路径提示词。

默认快捷键：

- `Alt+Shift+C`：在 Codex 中打开当前文件位置
- `Alt+Shift+X`：在 Codex 中打开当前项目

## 安装

推荐使用安装脚本直接安装到当前 IDEA 用户插件目录：

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\install-plugin.ps1
```

安装完成后重启 IntelliJ IDEA，然后在 `Settings | Plugins | Installed` 中搜索 `Open In Codex`。

也可以手动安装构建产物：

```text
build\distributions\idea-open-in-codex-0.1.1.zip
```

在 IDEA 中进入 `Settings | Plugins`，点击齿轮图标，选择 `Install Plugin from Disk...`，然后选择上面的 zip 文件。

## 本地构建

仓库内提供了 PowerShell 构建脚本，会使用本机已安装的 IntelliJ IDEA JBR 编译插件：

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\build-plugin.ps1
```

构建完成后会生成：

```text
build\distributions\idea-open-in-codex-0.1.1.zip
```

## 使用方式

打开任意项目后，可以通过以下入口使用：

- 顶部菜单：`Tools | Open File In Codex`
- 编辑器右键菜单：`Open File In Codex`
- 项目视图右键菜单：`Open File In Codex`
- 快捷键：`Alt+Shift+C`

项目入口：

- 顶部菜单：`Tools | Open Project In Codex`
- 项目视图右键菜单：`Open Project In Codex`
- 快捷键：`Alt+Shift+X`

项目入口生成的提示词格式：

```text
请查看当前项目：
项目：D:\path\to\project
```

## Codex URL

插件会打开类似下面的协议地址：

```text
codex://new?path=<项目路径>&prompt=<提示词>
```

文件入口生成的提示词格式：

```text
请查看当前文件：
文件：D:\path\to\File.java
行号：24-27
```

Windows 下会通过 PowerShell 调用 `Start-Process` 打开 Codex 桌面端协议。
