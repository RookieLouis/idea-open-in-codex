# Open In Codex

这是一个 IntelliJ IDEA 插件，用于从 IDEA 中把当前项目或当前文件位置打开到新的 Codex 桌面端会话。

## 此 Fork 的变更

本仓库 Fork 自 [Like-jian/idea-open-in-codex](https://github.com/Like-jian/idea-open-in-codex)。

修改了 `Open File In Codex`：在文件路径和行号信息后保留一个空白输入行，并预填一个普通空格。转到 Codex 后，可以直接在这一行输入自己的 prompt，无需手动换行，也不会插入 `Prompt:` 标签。

实现方式是在原有文件上下文末尾追加两个换行和一个普通空格（`"\n\n "`）。Codex 输入框会清理单个末尾换行；追加上述内容后，末尾输入段落可以保留。

- 已在 Windows、Rider 2026.2.1、Codex 桌面端 26.1013.4894.0 下验证，并在实际输入框中确认效果。
- `Copy File Prompt` 和 `Open Project In Codex` 保持原有行为。
- `Open File In Codex` 仍然打开新的 Codex 桌面端会话。

## 功能

- `Open File In Codex`：打开新的 Codex 会话，并带上当前项目路径、当前文件路径以及选中的行号范围；如果没有选中文本，则使用当前光标所在行。末尾保留空白输入行，供直接输入 prompt。
- `Copy File Prompt`：把与文件打开功能相同的当前文件路径和行号范围提示词复制到剪贴板，但不打开 Codex；可从编辑器和项目视图右键菜单使用。
- `Open Project In Codex`：打开新的 Codex 会话，并把当前项目路径作为 Codex 工作目录，同时附带当前项目路径提示词。

默认快捷键：

- `Alt+Shift+O`：在 Codex 中打开当前文件位置
- `Alt+Shift+P`：在 Codex 中打开当前项目

## Rider 的 Visual Assist 快捷键冲突

Rider 的 **Visual Assist** 快捷键方案已将 `Alt+Shift+O` 分配给文件搜索（`GotoFile`）。继承它的自定义方案也会继承该绑定，因此按键可能打开文件搜索窗口，而没有转到 Codex。

**已有自定义方案时**：先关闭 Rider，备份 `%APPDATA%\JetBrains\Rider<版本>\keymaps\` 下当前方案的 XML 文件，再将以下动作加入其现有的 `<keymap>` 元素内。保留原有方案名称、父方案及其他动作；如果这两个动作已存在，则修改原有动作，不要重复添加：

```xml
<action id="GotoFile" />
<action id="OpenFileInCodex">
  <keyboard-shortcut first-keystroke="alt shift O" />
</action>
```

空的 `GotoFile` 动作会清除继承的快捷键。如果你还为 `GotoFile` 自定义了其他快捷键，请保留这些条目，只移除 `Alt+Shift+O`，不要清空整个动作；`OpenFileInCodex` 已有的其他快捷键也应保留。重启 Rider，在代码编辑器中按 `Alt+Shift+O` 即可打开 Codex。此修复已在 Windows、Rider 2026.2.1 下实测确认。

**直接使用原版 Visual Assist 时**，可选用仓库内的 [Visual Assist - Codex 预设](<keymaps/Visual Assist - Codex.xml>)：

1. 安装 Open In Codex，并启用 Visual Assist Keymap 插件。
2. 关闭 Rider，将预设文件复制到 `%APPDATA%\JetBrains\Rider<版本>\keymaps\`。
3. 启动 Rider，在 **Settings | Keymap** 中选择 **Visual Assist - Codex**。

该预设需要手动选用，继承其余 Visual Assist 快捷键，并将 `Alt+Shift+O` 分配给 `Open File In Codex`。如果已有个人快捷键设置，请保留当前自定义方案，采用上面的 XML 调整方式。

## 安装

推荐使用安装脚本直接安装到当前 IDEA 用户插件目录：

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\install-plugin.ps1
```

安装完成后重启 IntelliJ IDEA，然后在 `Settings | Plugins | Installed` 中搜索 `Open In Codex`。

也可以手动安装构建产物：

```text
build\distributions\idea-open-in-codex-1.1.2.zip
```

在 IDEA 中进入 `Settings | Plugins`，点击齿轮图标，选择 `Install Plugin from Disk...`，然后选择上面的 zip 文件。

## 本地构建

仓库内提供了 PowerShell 构建脚本，会使用本机已安装的 IntelliJ IDEA JBR 编译插件：

```powershell
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\build-plugin.ps1
```

构建完成后会生成：

```text
build\distributions\idea-open-in-codex-1.1.2.zip
```

## 使用方式

打开任意项目后，可以通过以下入口使用：

- 顶部菜单：`Tools | Open File In Codex`
- 编辑器右键菜单：`Open File In Codex`
- 编辑器右键菜单：`Copy File Prompt`
- 项目视图右键菜单：`Open File In Codex`
- 项目视图右键菜单：`Copy File Prompt`
- 快捷键：`Alt+Shift+O`

项目入口：

- 顶部菜单：`Tools | Open Project In Codex`
- 项目视图右键菜单：`Open Project In Codex`
- 快捷键：`Alt+Shift+P`

项目入口生成的提示词格式：

```text
Project: D:\path\to\project
```

## Codex URL

插件会打开类似下面的协议地址：

```text
codex://new?path=<项目路径>&prompt=<提示词>
```

文件入口生成的提示词格式：

```text
File: D:\path\to\File.java
Line: 24-27
```

文件信息后还会保留一个预填了普通空格的空白输入行，用于继续输入 prompt。

Windows 下会通过 PowerShell 调用 `Start-Process` 打开 Codex 桌面端协议。
