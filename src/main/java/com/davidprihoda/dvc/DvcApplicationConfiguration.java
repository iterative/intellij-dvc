package com.davidprihoda.dvc;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DvcApplicationConfiguration implements Configurable {

    private final DvcApplicationState settings;

    private DvcApplicationConfigurationForm form;

    public DvcApplicationConfiguration() {
        settings = DvcApplicationState.getInstance();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Data Version Control";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (form == null) {
            form = new DvcApplicationConfigurationForm();
        }
        return form.getPanel();
    }

    @Override
    public boolean isModified() {
        return true; // TODO
    }

    @Override
    public void apply() throws ConfigurationException {
        form.save(settings);
    }

    @Override
    public void reset() {
        form.load(settings);
    }

    @Override
    public void disposeUIResources() {
        if (form != null) {
            form.getPanel().setVisible(false);
            form = null;
        }
    }

}
