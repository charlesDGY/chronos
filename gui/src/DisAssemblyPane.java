// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DisAssemblyPane.java

package src;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package src:
//			ExportBlocks, Assembly, CfgDataStruct

public class DisAssemblyPane extends JPanel
{

	public ExportBlocks exportblock;
	public String directory;
	public String filename;
	public Assembly assembly;
	public JTextArea discode;
	public JTextArea block;
	public String scode;
	public String sblock;
	public String cFiles[];

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public ExportBlocks getExportblock()
	{
		return exportblock;
	}

	public void setExportblock(ExportBlocks exportblock)
	{
		this.exportblock = exportblock;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public DisAssemblyPane()
	{
		directory = "";
		filename = "";
		exportblock = new ExportBlocks();
		discode = new JTextArea();
		block = new JTextArea();
		block.setEditable(false);
		discode.setEditable(false);
		scode = "";
		sblock = "";
		setLayout(new BorderLayout());
		add(block, "West");
		add(discode, "Center");
	}

	public void displayCfg()
	{
		exportblock.setDirectory(directory);
		exportblock.setFileNames(filename, directory);
		if (exportblock.loadInfoFile())
		{
			assembly = new Assembly(directory + "/" + filename);
			assembly.setFilename(filename);
			showDisCode();
			exportblock.DataFlow(filename);
		} else
		{
			JOptionPane.showMessageDialog(null, "The format of disassembly file is wrong!");
		}
	}

	public int getLine(String str)
	{
		char ch[] = str.toCharArray();
		int count = 0;
		int index = 0;
		for (int length = ch.length; index < length; index++)
			if (ch[index] == '\n')
				count++;

		return count;
	}

	public void showDisCode()
	{
		int len = exportblock.cfgvector.size();
		int index = 0;
		int procnum = -1;
		int line = 0;
		int i = 0;
		sblock = "";
		scode = "";
		for (; index < len; index++)
		{
			CfgDataStruct cfg = (CfgDataStruct)exportblock.cfgvector.get(index);
			if (procnum != cfg.procnum)
			{
				procnum = cfg.procnum;
				sblock += "Procedure: " + procnum;
				sblock += "\n";
				scode += "\n";
			}
			sblock += "Block: " + cfg.blocknum;
			String from = cfg.getAddress();
			String to = "";
			if (index != exportblock.cfgvector.size() - 1)
				to = ((CfgDataStruct)exportblock.cfgvector.get(index + 1)).address;
			String str = "";
			if (from.compareTo(to) < 0)
				str = assembly.getAssembly(from, to);
			scode += str;
			scode += "\n";
			sblock += "\n";
			line = getLine(str);
			for (i = 0; i < line; i++)
				sblock += "\n";

		}

		block.setText(sblock);
		discode.setText(scode);
	}

	public String[] getCFiles()
	{
		return cFiles;
	}

	public void setCFiles(String files[])
	{
		cFiles = files;
		exportblock.setCFiles(cFiles);
	}
}
