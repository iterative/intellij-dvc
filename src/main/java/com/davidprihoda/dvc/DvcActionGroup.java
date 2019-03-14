package com.davidprihoda.dvc;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class DvcActionGroup extends DefaultActionGroup implements DumbAware {

    @Override
    public void update(AnActionEvent event) {
        super.update(event);

        final Project project = event.getProject();
        final DataContext dataContext = event.getDataContext();
        final VirtualFile[] virtualFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        boolean enabled = DvcFileAction.isEnabled(project, virtualFiles);

        Presentation presentation = event.getPresentation();
        presentation.setVisible(enabled);
        presentation.setEnabled(enabled);
    }

}
