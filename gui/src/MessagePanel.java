// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MessagePanel.java

package src;

import java.awt.*;
import java.io.PrintStream;
import javax.swing.*;

public class MessagePanel extends JPanel
{

	JTabbedPane tabbedPane;
	JPanel wcetresult;
	JPanel simulateresult;
	JPanel dump;
	JTextArea message;
	JTextArea configuration;
	JTextArea areas[];
	JTextArea simareas[];
	String str[];
	String sims[];
	int index;
	int size;

	public MessagePanel()
	{
		super(new GridLayout(1, 1));
		size = 36;
		tabbedPane = new JTabbedPane();
		wcetresult = new JPanel();
		simulateresult = new JPanel();
		str = new String[size];
		sims = new String[size];
		areas = new JTextArea[size];
		simareas = new JTextArea[size];
		for (index = 0; index < size; index++)
		{
			areas[index] = new JTextArea("");
			simareas[index] = new JTextArea("");
		}

		configuration = new JTextArea();
		wcetresult.setLayout(new GridLayout(6, 6));
		simulateresult.setLayout(new GridLayout(6, 6));
		for (index = 0; index < size; index++)
		{
			wcetresult.add(areas[index]);
			simulateresult.add(simareas[index]);
			areas[index].setEditable(false);
			simareas[index].setEditable(false);
		}

		tabbedPane.addTab("Estimation Result", wcetresult);
		tabbedPane.addTab("Simulation Result", simulateresult);
		dump = new JPanel();
		dump.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(configuration, "Center");
		JScrollPane scroll = new JScrollPane(panel);
		dump.add(scroll, "Center");
		configuration.setEditable(false);
		add(tabbedPane);
		setPreferredSize(new Dimension(1000, 150));
	}

	public void showWectResult(String s[])
	{
		str = s;
		for (int i = 0; i < size; i++)
			areas[i].setText(s[i]);

		repaint();
	}

	public void showSimulate(String s[])
	{
		reset(false);
		sims = s;
		for (int i = 0; i < size; i++)
			simareas[i].setText(sims[i]);

		repaint();
	}

	public void showWaiting()
	{
		reset(true);
		str[0] = "Waiting";
		System.out.println(str[0]);
		repaint();
	}

	public void showFinish()
	{
		reset(true);
		str[0] = "ILP formulation complete!";
		System.out.println(str[0]);
		repaint();
	}

	public void reset(boolean b)
	{
		if (b)
		{
			for (int i = 0; i < size; i++)
				str[i] = "";

		} else
		{
			for (int i = 0; i < size; i++)
				sims[i] = "";

		}
		repaint();
	}

	public void showConfiguration(String s)
	{
		s = "Processor Configuration: \n" + s;
		configuration.setText(s);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		for (int i = 0; i < size; i++)
		{
			areas[i].setText(str[i]);
			simareas[i].setText(sims[i]);
		}

	}
}
