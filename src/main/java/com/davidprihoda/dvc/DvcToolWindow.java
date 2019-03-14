package com.davidprihoda.dvc;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DvcToolWindow {

    private static final Logger logger = LoggerFactory.getLogger(ToolRunner.class);

    private final ToolWindow toolWindow;
    private JPanel contentPanel;
    private JEditorPane outputPane;
    private JButton closeButton;
    private Content content;

    public DvcToolWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        closeButton.addActionListener(e -> close());
    }

    private void close() {
        toolWindow.getContentManager().removeContent(content, true);
    }

    public JPanel getContent() {
        return contentPanel;
    }

    public void bind(ToolRunner toolRunner, Content content) {
        this.content = content;
        insertText(String.join(" ", toolRunner.getCommandLine()));

        toolRunner.addListener(new ToolRunner.Listener() {
            @Override
            public void processStandardOutput(String line) {
                insertText(line);
            }

            @Override
            public void processStandardError(String line) {
                insertText(line);
            }

            @Override
            public void processException(Throwable throwable) {
                insertText(throwable.getMessage());
            }

            @Override
            public void completed(int returnCode) {
                insertText("Completed with exit code: " + returnCode);
            }
        });
    }

    private void insertText(String line) {
        Document document = outputPane.getDocument();
        try {
            document.insertString(document.getLength(),line + "\n", null);
        } catch (BadLocationException e) {
            logger.error("Error print output", e);
        }
    }

}
