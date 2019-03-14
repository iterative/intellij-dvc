package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

public class DvcToolWindowFactory implements ToolWindowFactory {

    private ToolWindow toolWindow;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }

    public static void addView(ToolWindow toolWindow, String title, ToolRunner toolRunner) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        DvcToolWindow dvcToolWindow = new DvcToolWindow(toolWindow);
        Content content = contentFactory.createContent(dvcToolWindow.getContent(), title, false);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(content);
        contentManager.setSelectedContent(content);
        dvcToolWindow.bind(toolRunner, content);
    }

}
