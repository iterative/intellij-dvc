package com.davidprihoda.dvc;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DvcPullOptionsDialog extends DialogWrapper {

    private JPanel contentPane;
    private JTextField remoteField;
    private JCheckBox allBranchesField;
    private JCheckBox allTagsField;
    private JCheckBox allDepsField;

    public DvcPullOptionsDialog(Project project) {
        super(project);
        init();
        setTitle("DVC Pull Options");
    }

    public String getRemoteField() {
        return remoteField.getText().trim();
    }

    public boolean getAllBranchesField() {
        return allBranchesField.isSelected();
    }

    public boolean getAllTagsField() {
        return allTagsField.isSelected();
    }

    public boolean getAllDepsField() {
        return allDepsField.isSelected();
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return remoteField;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public boolean restoreShowStoreAndGet() {
        restore();
        boolean status = super.showAndGet();
        if (status) {
            store();
        }
        return status;
    }

    private void restore() {
        DvcApplicationState instance = DvcApplicationState.getInstance();
        remoteField.setText(instance.getOptionsRemote());
        allBranchesField.setSelected(instance.isOptionsAllBranches());
        allTagsField.setSelected(instance.isOptionsAllTags());
        allDepsField.setSelected(instance.isOptionsWithDeps());
    }

    private void store() {
        DvcApplicationState instance = DvcApplicationState.getInstance();
        String remote = getRemoteField();
        instance.setOptionsRemote(StringUtils.isNoneBlank(remote) ? remote : null);
        instance.setOptionsAllBranches(getAllBranchesField());
        instance.setOptionsAllTags(getAllTagsField());
        instance.setOptionsWithDeps(getAllDepsField());
    }

}
