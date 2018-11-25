package com.davidprihoda.dvc;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DvcReferenceSet extends FileReferenceSet {
    private static final Logger LOG = Logger.getInstance(DvcReferenceSet.class);

    public DvcReferenceSet(@NotNull PsiElement element) {
        super(element);
    }

    @Override
    protected void reparse() {
        List<FileReference> referencesList = this.reparse(this.getPathString(), this.getStartInElement());
        this.myReferences = referencesList.toArray(new FileReference[referencesList.size()]);
    }

    @Override
    protected List<FileReference> reparse(String str, int startInElement) {
        TextRange valueRange = TextRange.from(startInElement, str.length());
        return Collections.singletonList(createFileReference(valueRange, 0, str));
    }

}
