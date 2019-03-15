package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;

import java.util.Collections;
import java.util.List;

public class DvcPushFileAction extends DvcFileAction {

    public DvcPushFileAction() {
        super("push");
    }

    @Override
    protected List<String> askForActionOptions(Project project) {
        return Collections.emptyList();
    }

}
