package com.davidprihoda.dvc;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

public class DvcActionGroup extends DefaultActionGroup implements DumbAware {

    @Override
    public void update(AnActionEvent event) {
        super.update(event);

        Presentation presentation = event.getPresentation();
        Project project = event.getProject();
        if (project == null || !project.isOpen()) {
            presentation.setVisible(false);
            presentation.setEnabled(false);
        }
        else {
            presentation.setVisible(true);
            presentation.setEnabled(true);
        }
    }

}
