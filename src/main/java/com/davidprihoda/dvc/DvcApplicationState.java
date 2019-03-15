package com.davidprihoda.dvc;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@com.intellij.openapi.components.State(name = "dvc_app_settings", storages = @Storage("dvc.xml"))
public class DvcApplicationState implements ApplicationComponent, PersistentStateComponent<DvcApplicationState> {

    private String dvcExecutable = "dvc";
    private String optionsRemote;
    private boolean optionsAllBranches;
    private boolean optionsAllTags;
    private boolean optionsWithDeps;

    public String getDvcExecutable() {
        return dvcExecutable;
    }

    public void setDvcExecutable(String dvcExecutable) {
        this.dvcExecutable = dvcExecutable;
    }

    public String getOptionsRemote() {
        return optionsRemote;
    }

    public void setOptionsRemote(String optionsRemote) {
        this.optionsRemote = optionsRemote;
    }

    public boolean isOptionsAllBranches() {
        return optionsAllBranches;
    }

    public void setOptionsAllBranches(boolean optionsAllBranches) {
        this.optionsAllBranches = optionsAllBranches;
    }

    public boolean isOptionsAllTags() {
        return optionsAllTags;
    }

    public void setOptionsAllTags(boolean optionsAllTags) {
        this.optionsAllTags = optionsAllTags;
    }

    public boolean isOptionsWithDeps() {
        return optionsWithDeps;
    }

    public void setOptionsWithDeps(boolean optionsWithDeps) {
        this.optionsWithDeps = optionsWithDeps;
    }

    @Nullable
    @Override
    public DvcApplicationState getState() {
        return this;
    }

    @Override
    public void loadState(DvcApplicationState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static DvcApplicationState getInstance() {
        return ServiceManager.getService(DvcApplicationState.class);
    }

}
