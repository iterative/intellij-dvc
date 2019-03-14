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

    public String getDvcExecutable() {
        return dvcExecutable;
    }

    public void setDvcExecutable(String dvcExecutable) {
        this.dvcExecutable = dvcExecutable;
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
