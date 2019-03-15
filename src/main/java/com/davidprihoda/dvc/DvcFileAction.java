package com.davidprihoda.dvc;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public abstract class DvcFileAction extends DumbAwareAction {

    private final String action;

    public DvcFileAction(String action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        final DataContext dataContext = event.getDataContext();
        Project project = event.getProject();
        final VirtualFile[] virtualFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if (isEnabled(project, virtualFiles)) {
            List<String> options = askForActionOptions(project);
            if (options != null) {
                List<String> filePaths = convertFilesToTargetArguments(virtualFiles);
                DvcUtils.runTool(event.getProject(), action, options, filePaths);
            }
        }
    }

    protected abstract List<String> askForActionOptions(Project project);

    private static List<String> convertFilesToTargetArguments(VirtualFile[] virtualFiles) {
        List<String> paths = new ArrayList<>();
        for (VirtualFile virtualFile : virtualFiles) {
            String extension = virtualFile.getExtension();
            if ("dvcfile".equals(virtualFile.getPath().toLowerCase()) || (extension != null && "dvc".equals(extension.toLowerCase()))) {
                paths.add(virtualFile.getPath());
            }
        }
        return paths;
    }

    public static boolean isEnabled(Project project, VirtualFile[] virtualFiles) {
        return project != null && project.isOpen() && virtualFiles != null;
    }

}
