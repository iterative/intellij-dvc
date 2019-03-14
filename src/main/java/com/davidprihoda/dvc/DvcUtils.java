package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

public class DvcUtils {

    public static void runTool(Project project, String action) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DVC Output");
        String dvcExecutable = DvcApplicationState.getInstance().getDvcExecutable();
        ToolRunner toolRunner = new ToolRunner(project.getBaseDir().getPath(), dvcExecutable, action);
        DvcToolWindowFactory.addView(toolWindow, action, toolRunner);
        toolRunner.start();
    }

}
