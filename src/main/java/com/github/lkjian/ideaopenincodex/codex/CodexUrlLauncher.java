package com.github.lkjian.ideaopenincodex.codex;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class CodexUrlLauncher {

    /**
     * Codex 桌面端注册的新会话协议地址。
     */
    private static final String CODEX_NEW_SESSION_SCHEME = "codex://new";

    private CodexUrlLauncher() {
    }

    public static void openNewSession(String projectPath, String prompt) throws Exception {
        String url = CODEX_NEW_SESSION_SCHEME
                + "?path=" + encode(projectPath)
                + "&prompt=" + encode(prompt);
        openUrl(url);
    }

    public static void openProject(String projectPath) throws Exception {
        String url = CODEX_NEW_SESSION_SCHEME
                + "?path=" + encode(projectPath);
        openUrl(url);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static void openUrl(String url) throws Exception {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (osName.contains("windows")) {
            new ProcessBuilder(
                    "powershell.exe",
                    "-NoProfile",
                    "-ExecutionPolicy",
                    "Bypass",
                    "-Command",
                    "Start-Process '" + url.replace("'", "''") + "'"
            ).start();
            return;
        }

        if (osName.contains("mac")) {
            new ProcessBuilder("open", url).start();
            return;
        }

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI.create(url));
        } else {
            new ProcessBuilder("xdg-open", url).start();
        }
    }
}
