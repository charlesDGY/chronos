// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CenterPane.java

package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package src:
//			Tree, CfgGraph, ExportBlocks

public class CenterPane
{

	public JSplitPane mainPane;
	public JPanel panelRight;
	public CfgGraph cfggraph;
	public ExportBlocks exportBlock;
	public String cfilename;
	public String directory;
	public Vector cfgvector;
	public JScrollPane scrolleft;
	public Tree tree;

	public JSplitPane getMainPane()
	{
		return mainPane;
	}

	public void setMainPane(JSplitPane mainPane)
	{
		this.mainPane = mainPane;
	}

	public ExportBlocks getExportBlock()
	{
		return exportBlock;
	}

	public void setExportBlock(ExportBlocks exportBlock)
	{
		this.exportBlock = exportBlock;
	}

	public CenterPane()
	{
		panelRight = new JPanel();
		tree = new Tree();
		mainPane = new JSplitPane(1, tree, panelRight);
		mainPane.setOneTouchExpandable(true);
		mainPane.setDividerLocation(500);
		panelRight.setBackground(Color.WHITE);
		cfggraph = new CfgGraph();
		panelRight.setLayout(new BorderLayout());
		panelRight.add(cfggraph, "Center");
	}

	public void display(String dirPath)
	{
		cfggraph.loadGraph(exportBlock.cfgvector, exportBlock.numberproc);
		cfggraph.validate();
		cfggraph.repaint();
		tree.init(dirPath);
		mainPane.validate();
	}

	public String getDisDirectory(String directory)
	{
		String dis = "";
		int index;
		for (index = 0; directory.charAt(index) != '.'; index++);
		dis = dis + directory.substring(0, index) + ".dis";
		return dis;
	}
}
