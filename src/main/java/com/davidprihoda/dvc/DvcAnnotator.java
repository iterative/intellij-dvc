package com.davidprihoda.dvc;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;
import org.jetbrains.yaml.psi.impl.YAMLQuotedTextImpl;

public class DvcAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if(!(element instanceof YAMLKeyValueImpl)){
            return;
        }
        YAMLKeyValueImpl keyValueElem = (YAMLKeyValueImpl)element;
        if (keyValueElem.getKeyText().equals("path")) {
            PsiElement valueElem = keyValueElem.getValue();
            if (valueElem != null) {
                String path = valueElem.getText();
                TextRange range = new TextRange(valueElem.getTextRange().getStartOffset(), valueElem.getTextRange().getEndOffset());
                String description = "Path: '" + path + "', Type: " + valueElem.getClass().getSimpleName();
                holder.createInfoAnnotation(range, description).setTextAttributes(DefaultLanguageHighlighterColors.LABEL);
            }
        }


    }
}

