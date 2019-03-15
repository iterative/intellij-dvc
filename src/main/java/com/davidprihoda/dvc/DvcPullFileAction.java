package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DvcPullFileAction extends DvcFileAction {

    public DvcPullFileAction() {
        super("pull");
    }

    @Override
    protected List<String> askForActionOptions(Project project) {
        DvcPullOptionsDialog dialog = new DvcPullOptionsDialog(project);
        if (dialog.restoreShowStoreAndGet()) {
            List<String> options = new ArrayList<>();
            String remoteField = dialog.getRemoteField();
            if (StringUtils.isNotBlank(remoteField)) {
                options.add("--remote");
                options.add(remoteField.trim());
            }
            if (dialog.getAllBranchesField()) {
                options.add("--all-branches");
            }
            if (dialog.getAllTagsField()) {
                options.add("--all-tags");
            }
            if (dialog.getAllDepsField()) {
                options.add("--with-deps");
            }
            return options;
        } else {
            return null;
        }
    }

}
