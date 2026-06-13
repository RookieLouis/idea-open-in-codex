package com.github.lkjian.ideaopenincodex.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class CodexMessages {

    private CodexMessages() {
    }

    public static String projectPrompt(String projectPath) {
        return message("prompt.project", projectPath);
    }

    public static String filePrompt(String filePath, String lineRange) {
        return message("prompt.file", filePath, lineRange);
    }

    private static String message(String key, Object... arguments) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.github.lkjian.ideaopenincodex.i18n.CodexMessages",
                Locale.getDefault()
        );
        return MessageFormat.format(bundle.getString(key), arguments);
    }
}
