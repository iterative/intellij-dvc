package com.davidprihoda.dvc;

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
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Browse for DVC Home Directory");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        int result = fc.showOpenDialog(dvcExecutableBrowseButton);
        if (result == JFileChooser.APPROVE_OPTION) {
            setDvcExecutable(fc.getSelectedFile().getAbsolutePath());
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
