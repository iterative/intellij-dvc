package com.davidprihoda.dvc;

import com.ansorgit.plugins.bash.file.BashFileType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class DvcCommandLanguageInjector implements LanguageInjector {
    private static final Logger LOG = Logger.getInstance(DvcCommandLanguageInjector.class);

    @Override
    public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar) {
        if(host.getContainingFile() == null ||
                host.getContainingFile().getVirtualFile() == null ||
                host.getContainingFile().getVirtualFile().getFileType() != DvcFileType.INSTANCE) {
            return;
        }
        PsiElement parent = host.getParent();
//        LOG.info("Parent of: '"+host.getText()+"' ("+host.getClass().getSimpleName()+") is '"+parent.getText()+"' ("+parent.getClass().getSimpleName()+")");
//        if (parent instanceof YAMLCompoundValueImpl) {
//            parent = parent.getParent();
//            LOG.info("Moving parent to: '"+parent.getText()+"'");
//        }
        if (parent instanceof YAMLKeyValueImpl) {
            YAMLKeyValueImpl keyValue = (YAMLKeyValueImpl)parent;
            //LOG.info("Key value: "+keyValue.getValue());
            if(keyValue.getKeyText().equals("cmd")) {
                TextRange contentRange = ElementManipulators.getValueTextRange(host);
                //LOG.info("Adding range: "+contentRange);
                if (!contentRange.isEmpty()) {
                    injectionPlacesRegistrar.addPlace(BashFileType.BASH_LANGUAGE, contentRange, null, null);
                }
            }
        }

    }
}