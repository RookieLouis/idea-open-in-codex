package com.github.lkjian.ideaopenincodex.actions;

import com.github.lkjian.ideaopenincodex.codex.CodexUrlLauncher;
import com.github.lkjian.ideaopenincodex.i18n.CodexMessages;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

public final class OpenCurrentFileInCodexAction extends AnAction {

    /**
     * 未选中文本时用于提示 Codex 的默认行号范围。
     */
    private static final int SINGLE_LINE_RANGE_SIZE = 1;

    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (project == null || file == null || file.isDirectory()) {
            return;
        }

        String projectPath = project.getBasePath();
        if (projectPath == null || projectPath.isBlank()) {
            Messages.showErrorDialog(project, "Cannot resolve project path.", "Open In Codex");
            return;
        }

        LineRange lineRange = resolveLineRange(event.getData(CommonDataKeys.EDITOR));
        // Keep a separate final input paragraph with one ordinary space.
        String prompt = CodexMessages.filePrompt(file.getPath(), lineRange.toDisplayText()) + "\n\n ";

        try {
            CodexUrlLauncher.openNewSession(projectPath, prompt);
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "Failed to open Codex:\n" + ex.getMessage(), "Open In Codex");
        }
    }

    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        event.getPresentation().setEnabledAndVisible(project != null && file != null && !file.isDirectory());
    }

    private static LineRange resolveLineRange(Editor editor) {
        if (editor == null) {
            return new LineRange(1, SINGLE_LINE_RANGE_SIZE);
        }

        if (editor.getSelectionModel().hasSelection()) {
            LogicalPosition start = editor.offsetToLogicalPosition(editor.getSelectionModel().getSelectionStart());
            LogicalPosition end = editor.offsetToLogicalPosition(editor.getSelectionModel().getSelectionEnd());
            int endLine = end.column == 0 && end.line > start.line ? end.line : end.line + 1;
            return new LineRange(start.line + 1, Math.max(start.line + 1, endLine));
        }

        int line = editor.getCaretModel().getLogicalPosition().line + 1;
        return new LineRange(line, line);
    }

    private record LineRange(int startLine, int endLine) {
        String toDisplayText() {
            if (startLine == endLine) {
                return String.valueOf(startLine);
            }
            return startLine + "-" + endLine;
        }
    }
}
