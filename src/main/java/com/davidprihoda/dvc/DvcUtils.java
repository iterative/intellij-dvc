package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DvcUtils {

    public static void runTool(Project project, String action, List<String> options, List<String> files) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DVC Output");
        String dvcExecutable = DvcApplicationState.getInstance().getDvcExecutable();
        List<String> commandLine = new ArrayList<>(Arrays.asList(dvcExecutable, action));
        commandLine.addAll(options);
        commandLine.addAll(files);
        ToolRunner toolRunner = new ToolRunner(project.getBaseDir().getPath(), commandLine.toArray(new String[0]));
        DvcToolWindowFactory.addView(toolWindow, action, toolRunner);
        toolRunner.start();
    }

}
