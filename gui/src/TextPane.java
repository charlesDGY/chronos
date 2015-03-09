// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TextPane.java

package src;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TextPane extends JPanel
{

	public JTextArea textcode;
	public JTextArea textline;
	public String code;
	public String line;
	public int fontheight;

	public TextPane()
	{
		textcode = new JTextArea();
		textcode.setEditable(false);
		textline = new JTextArea();
		textline.setEditable(false);
		code = "";
		line = "";
		setLayout(new BorderLayout());
		add(textcode, "Center");
		add(textline, "West");
		textline.setBackground(Color.green);
		java.awt.Font f = textcode.getFont();
		FontMetrics fm = textcode.getFontMetrics(f);
		fontheight = fm.getHeight();
	}

	public void clear()
	{
		textcode.setText("");
		textline.setText("");
		repaint();
		validate();
	}

	public void displayFile(String sPath)
	{
		FileInputStream inputfile = null;
		line = "";
		code = "";
		try
		{
			inputfile = new FileInputStream(sPath);
			int size = inputfile.available();
			byte bytes[] = new byte[size];
			inputfile.read(bytes);
			code = new String(bytes);
			int lines = 1;
			for (int i = 0; i < size; i++)
				if (bytes[i] == 10)
				{
					line += lines;
					line += 10;
					lines++;
				}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				inputfile.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (code != null)
		{
			textcode.setText(code);
			textline.setText(line);
		}
		repaint();
		validate();
		return;
	}
}
