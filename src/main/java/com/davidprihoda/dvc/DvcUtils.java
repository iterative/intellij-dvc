package com.davidprihoda.dvc;

public class DvcUtils {

    public String getDvcExecutable() {
        return DvcApplicationState.getInstance().getDvcExecutable();
    }

}
