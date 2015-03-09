// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Tree.java

package src;

import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import javax.swing.*;

// Referenced classes of package src:
//			DynamicTree, TextPane, ConstraintPane

public class Tree extends JPanel
{
	public class nodeInfo
	{

		public String name;

		public String getName()
		{
			return name;
		}

		public String toString()
		{
			return name;
		}

		public nodeInfo(String s)
		{
			name = s;
		}
	}


	String sTitle;
	String dirPath;
	JSplitPane splitPane;
	DynamicTree dTree;
	JScrollPane textView;
	TextPane textPanel;
	String CFiles[];
	ConstraintPane pane;

	public ConstraintPane getPane()
	{
		return pane;
	}

	public void setPane(ConstraintPane pane)
	{
		this.pane = pane;
	}

	public Tree()
	{
		super(new GridLayout(1, 0));
		sTitle = "Source";
		dTree = new DynamicTree();
		dTree.setM_Tree(this);
		dTree.setEnabled(false);
		textPanel = new TextPane();
		textView = new JScrollPane(textPanel);
		textView.setBackground(Color.WHITE);
		splitPane = new JSplitPane(1, dTree, textView);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(100);
		Dimension minimumSize = new Dimension(300, 200);
		splitPane.setPreferredSize(new Dimension(900, 800));
		add(splitPane, "Center");
	}

	public void init(String dirPath)
	{
		dTree.setEnabled(true);
		this.dirPath = dirPath;
		removeNode();
		addNode();
	}

	public void removeNode()
	{
		dTree.clear();
		textPanel.clear();
	}

	public void addNode()
	{
		collectCFile(dirPath);
		for (int i = 0; i < CFiles.length; i++)
			dTree.addObject(new nodeInfo(CFiles[i]));

		textPanel.displayFile(dirPath + "/" + CFiles[0]);
	}

	public boolean isCFile(String filename)
	{
		int len = filename.length();
		if (len < 2)
			return false;
		return filename.charAt(len - 1) == 'c' && filename.charAt(len - 2) == '.';
	}

	public void collectCFile(String dirPath)
	{
		int num = 0;
		File file = new File(dirPath);
		String children[] = file.list();
		int i;
		for (i = 0; i < children.length; i++)
			if (isCFile(children[i]))
				num++;

		CFiles = null;
		CFiles = new String[num];
		i = 0;
		int j = 0;
		for (; i < children.length; i++)
			if (isCFile(children[i]))
			{
				CFiles[j] = new String(children[i]);
				j++;
			}

		sortCFiles();
	}

	public void sortCFiles()
	{
		int len = CFiles.length;
		for (int i = 0; i < len; i++)
		{
			for (int j = i + 1; j < len; j++)
				if (CFiles[i].compareTo(CFiles[j]) > 0)
				{
					String temp = CFiles[i];
					CFiles[i] = CFiles[j];
					CFiles[j] = temp;
				}

		}

	}

	public int getFileIndex(String file)
	{
		for (int i = 0; i < CFiles.length; i++)
			if (CFiles[i].compareTo(file) == 0)
				return i;

		return -1;
	}

	public void updateSource(Object object)
	{
		String filename = ((nodeInfo)object).getName();
		System.out.println(dirPath + "/" + filename);
		textPanel.displayFile(dirPath + "/" + filename);
	}
}
