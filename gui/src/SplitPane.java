// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SplitPane.java

package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

// Referenced classes of package src:
//			CenterPane, DisAssemblyPane

public class SplitPane
{

	public CenterPane center;
	public JSplitPane splitPane;
	public String openfileName;
	public String directory;
	public String dirName;
	public String dirPath;
	public DisAssemblyPane disPane;

	public SplitPane()
	{
		initComponents();
	}

	public void initComponents()
	{
		center = new CenterPane();
		disPane = new DisAssemblyPane();
		JPanel dispanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(disPane);
		dispanel.setLayout(new BorderLayout());
		dispanel.add(scrollPane, "Center");
		splitPane = new JSplitPane(1, center.getMainPane(), dispanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(800);
		Dimension minimumSize = new Dimension(500, 500);
		center.getMainPane().setMinimumSize(minimumSize);
		splitPane.setPreferredSize(new Dimension(900, 500));
	}

	public JSplitPane getSplitPane()
	{
		return splitPane;
	}

	public void setPath(String dirPath, String dirName)
	{
		this.dirPath = dirPath;
		this.dirName = dirName;
		disPane.setDirectory(dirPath);
		disPane.setFilename(dirName);
	}

	public void display()
	{
		disPane.displayCfg();
		center.setExportBlock(disPane.getExportblock());
		center.display(dirPath);
	}
}
