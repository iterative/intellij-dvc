package com.davidprihoda.dvc.structure;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CellRenderer extends JLabel implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Node node = (Node) ((DefaultMutableTreeNode) value).getUserObject();
        setText(node.getText());
        setToolTipText(node.getText());
        return this;
    }
}
