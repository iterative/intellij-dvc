package com.davidprihoda.dvc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLFile;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.PlatformPatterns.psiFile;

public class DvcReferenceContributor extends PsiReferenceContributor {
    private static final Logger LOG = Logger.getInstance(DvcReferenceContributor.class);

    /**
     * Registers new references provider for PSI element.
     *
     * @param psiReferenceRegistrar reference provider
     */
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar) {
        psiReferenceRegistrar.registerReferenceProvider(psiElement().inFile(psiFile(YAMLFile.class)),
                new IgnoreReferenceProvider());
    }

    private static class IgnoreReferenceProvider extends PsiReferenceProvider {
        /**
         * Returns references for given @{link PsiElement}.
         *
         * @param psiElement        current element
         * @param processingContext context
         * @return {@link PsiReference} list
         */
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement,
                                                     @NotNull ProcessingContext processingContext) {
            if(psiElement instanceof YAMLKeyValueImpl){
                YAMLKeyValueImpl keyValueElem = (YAMLKeyValueImpl) psiElement;
                String key = keyValueElem.getKeyText();
                if ((key.equals("path") || key.equals("cmd")) && keyValueElem.getValue() != null) {
                    return new DvcReferenceSet(keyValueElem.getValue()).getAllReferences();
                }
            }
            return PsiReference.EMPTY_ARRAY;
        }
    }
}
