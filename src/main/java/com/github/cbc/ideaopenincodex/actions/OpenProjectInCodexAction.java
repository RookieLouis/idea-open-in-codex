package com.github.cbc.ideaopenincodex.actions;

import com.github.cbc.ideaopenincodex.codex.CodexUrlLauncher;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public final class OpenProjectInCodexAction extends AnAction {

    @Override
    public ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }

        String projectPath = project.getBasePath();
        if (projectPath == null || projectPath.isBlank()) {
            Messages.showErrorDialog(project, "Cannot resolve project path.", "Open In Codex");
            return;
        }

        String prompt = "请查看当前项目：\n项目：" + projectPath;
        try {
            CodexUrlLauncher.openNewSession(projectPath, prompt);
        } catch (Exception ex) {
            Messages.showErrorDialog(project, "Failed to open Codex:\n" + ex.getMessage(), "Open In Codex");
        }
    }

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(event.getProject() != null);
    }
}
