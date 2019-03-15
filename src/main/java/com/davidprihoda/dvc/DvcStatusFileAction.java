package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;

import java.util.Collections;
import java.util.List;

public class DvcStatusFileAction extends DvcFileAction {

    public DvcStatusFileAction() {
        super("status");
    }

    @Override
    protected List<String> askForActionOptions(Project project) {
        return Collections.emptyList();
    }

}
