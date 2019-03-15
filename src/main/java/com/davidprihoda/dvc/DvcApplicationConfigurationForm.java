package com.davidprihoda.dvc;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;

public class DvcApplicationConfigurationForm {
    private JPanel panel;
    private JTextField dvcExecutableField;
    private JButton dvcExecutableBrowseButton;

    public DvcApplicationConfigurationForm() {
        dvcExecutableBrowseButton.addActionListener(e -> browseForDvcHome());
    }

    public JComponent getPanel() {
        return panel;
    }

    public void browseForDvcHome() {
        VirtualFile virtualFile = FileChooser.chooseFile(
                new FileChooserDescriptor(true, false, false, false, false, false),
                null, null);
        if (virtualFile != null) {
            setDvcExecutable(virtualFile.getPath());
        }
    }

    private void setDvcExecutable(String path) {
        dvcExecutableField.setText(path);
    }

    public void load(DvcApplicationState settings) {
        setDvcExecutable(settings.getDvcExecutable());
    }

    public void save(DvcApplicationState settings) {
        settings.setDvcExecutable(dvcExecutableField.getText());
    }

}
