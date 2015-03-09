// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DynamicTree.java

package src;

import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

// Referenced classes of package src:
//			Tree

public class DynamicTree extends JPanel
	implements TreeSelectionListener
{

	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	public JTree tree;
	public Tree m_Tree;
	private Toolkit toolkit;

	public Tree getM_Tree()
	{
		return m_Tree;
	}

	public void setM_Tree(Tree tree)
	{
		m_Tree = tree;
	}

	public DynamicTree()
	{
		super(new GridLayout(1, 0));
		toolkit = Toolkit.getDefaultToolkit();
		rootNode = new DefaultMutableTreeNode("Source");
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(1);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}

	public void clear()
	{
		rootNode.removeAllChildren();
		treeModel.reload();
	}

	public void removeNode()
	{
		for (int i = 0; i < treeModel.getChildCount(rootNode); i++)
			treeModel.removeNodeFromParent((MutableTreeNode)treeModel.getChild(rootNode, i));

	}

	public void removeCurrentNode()
	{
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null)
		{
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)currentSelection.getLastPathComponent();
			MutableTreeNode parent = (MutableTreeNode)currentNode.getParent();
			if (parent != null)
			{
				treeModel.removeNodeFromParent(currentNode);
				return;
			}
		}
		toolkit.beep();
	}

	public DefaultMutableTreeNode addObject(Object child)
	{
		DefaultMutableTreeNode parentNode = null;
		parentNode = rootNode;
		return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child)
	{
		return addObject(parent, child, false);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible)
	{
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		if (parent == null)
			parent = rootNode;
		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
		if (shouldBeVisible)
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		return childNode;
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if (node == null || node == rootNode)
			return;
		Object object = node.getUserObject();
		if (node.isLeaf())
			m_Tree.updateSource(object);
	}
}
