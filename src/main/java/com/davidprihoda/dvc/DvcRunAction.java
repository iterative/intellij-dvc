package com.davidprihoda.dvc;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

public class DvcRunAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(DvcRunAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        DvcUtils.runTool(e.getProject(), "status");
    }

}
