package com.davidprihoda.dvc.structure;

import com.intellij.ui.treeStructure.SimpleTree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class DvcStructureToolWindow {
    private SimpleTree content;

    DvcStructureToolWindow() {

        DefaultMutableTreeNode finalTxt = createDvcNode("final.txt.dvc", "cp hello_world.txt final.txt");
        DefaultMutableTreeNode helloWorldTxt = createDvcNode("hello_world.txt.dvc", "paste hello.txt world.txt > hello_world.txt", finalTxt);
        DefaultMutableTreeNode worldTxt = createDvcNode("world.txt.dvc", "yes \"World\" | head -100 > world.txt", helloWorldTxt);

        DefaultMutableTreeNode threeTxt = createDvcNode("three.txt.dvc", "");
        DefaultMutableTreeNode world2Txt = createDvcNode("world2.txt.dvc", "");
        DefaultMutableTreeNode helloTxt = createDvcNode("hello.txt.dvc", "yes \"Hello\" | head -100 > hello.txt", threeTxt, world2Txt);

        DefaultMutableTreeNode tree = createNodeFromText("dvc files");
        tree.add(worldTxt);
        tree.add(helloTxt);

        TreeModel treeModel = new DefaultTreeModel(tree);
        SimpleTree fullTree = new SimpleTree(treeModel);

        content = fullTree;
        content.setCellRenderer(new CellRenderer());
    }

    public SimpleTree getContent() {
        return content;
    }

    DefaultMutableTreeNode createNodeFromText(String text) {
        return new DefaultMutableTreeNode(new Node(text));
    }

    DefaultMutableTreeNode createDvcNode(String filename, String command, DefaultMutableTreeNode... deps) {
        DefaultMutableTreeNode result = createNodeFromText(filename);
        result.add(createNodeFromText("command: " + command));
        if (deps != null && deps.length > 0) {
            DefaultMutableTreeNode depTree = createNodeFromText("deps");
            for (DefaultMutableTreeNode e : deps) {
                depTree.add(e);
            }
            result.add(depTree);
        }
        return result;
    }
}
