// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConstraintPane.java

package src;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

// Referenced classes of package src:
//			ConstraintRules, ExportBlocks

public class ConstraintPane extends JPanel
	implements ListSelectionListener
{
	public class DeleteListener
		implements ActionListener
	{

		public void actionPerformed(ActionEvent arg0)
		{
			int index = list.getSelectedIndex();
			if (index < 0)
				return;
			listModel.remove(index);
			int size = listModel.getSize();
			if (size != 0)
			{
				if (index == listModel.getSize())
					index--;
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		}

		public DeleteListener()
		{
		}
	}

	public class AddListener
		implements ActionListener, DocumentListener
	{

		private boolean alreadyEnabled;
		private JButton button;

		public void actionPerformed(ActionEvent e)
		{
			String name = constraint.getText();
			if (name.equals(""))
			{
				constraint.requestFocusInWindow();
				constraint.selectAll();
				return;
			}
			if (!doText())
			{
				JOptionPane.showMessageDialog(null, "Constraint is invalid!");
				return;
			}
			if (alreadyInList(constraint.getText()))
			{
				JOptionPane.showMessageDialog(null, "Duplicate Constraint!");
				return;
			}
			int index = list.getSelectedIndex();
			if (index == -1)
				index = 0;
			else
				index++;
			String file = "  : " + cFiles[comboFile.getSelectedIndex()];
			listModel.insertElementAt(constraint.getText() + file, index);
			constraint.requestFocusInWindow();
			constraint.setText("");
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}

		protected boolean alreadyInList(String name)
		{
			return listModel.contains(name);
		}

		public void insertUpdate(DocumentEvent arg0)
		{
			enableButton();
		}

		public void removeUpdate(DocumentEvent e)
		{
			handleEmptyTextField(e);
		}

		public void changedUpdate(DocumentEvent e)
		{
			if (!handleEmptyTextField(e))
				enableButton();
		}

		private void enableButton()
		{
			if (!alreadyEnabled && !filename.equals(""))
				button.setEnabled(true);
		}

		private boolean handleEmptyTextField(DocumentEvent e)
		{
			if (e.getDocument().getLength() <= 0)
			{
				alreadyEnabled = false;
				return true;
			} else
			{
				return false;
			}
		}

		public AddListener(JButton button)
		{
			alreadyEnabled = false;
			this.button = button;
		}
	}


	private JList list;
	private DefaultListModel listModel;
	private static final String addString = "Add a Constraint";
	private static final String deleteString = "Delete a Constraint";
	private static final String invalidString = "Constraint is invalid!";
	private static final String duplicateString = "Duplicate Constraint!";
	private static final String comma = "  : ";
	private String cFiles[];
	private int ifile;
	private JComboBox comboFile;
	private JButton addButton;
	private JButton deleteButton;
	private JTextField constraint;
	public ConstraintRules constraints;
	public int constraintnums;
	public String directory;
	public String filename;
	public String constraintsFileName;
	public JScrollPane listScrollPane;
	public ExportBlocks exportblock;

	public int getIfile()
	{
		return ifile;
	}

	public void setIfile(int ifile)
	{
		this.ifile = ifile;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public ConstraintPane()
	{
		super(new BorderLayout());
		filename = "";
		initConstraintsArray();
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(0);
		list.addListSelectionListener(this);
		listScrollPane = new JScrollPane(list);
		addButton = new JButton("Add a Constraint");
		AddListener addListener = new AddListener(addButton);
		addButton.setActionCommand("Add a Constraint");
		addButton.addActionListener(addListener);
		addButton.setEnabled(true);
		deleteButton = new JButton("Delete a Constraint");
		deleteButton.setActionCommand("Delete a Constraint");
		deleteButton.addActionListener(new DeleteListener());
		deleteButton.setEnabled(true);
		constraint = new JTextField(20);
		constraint.addActionListener(addListener);
		constraint.getDocument().addDocumentListener(addListener);
		comboFile = new JComboBox();
		setLayout(new BorderLayout());
		add(listScrollPane, "Center");
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new GridLayout(2, 2));
		bottomPane.add(constraint);
		bottomPane.add(comboFile);
		bottomPane.add(addButton);
		bottomPane.add(deleteButton);
		add(bottomPane, "South");
	}

	public void addComponent()
	{
		comboFile.removeAllItems();
		for (int i = 0; i < cFiles.length; i++)
			comboFile.addItem(cFiles[i]);

	}

	public void initListModel()
	{
		Scanner scanner = null;
		File file = null;
		file = new File(constraintsFileName);
		if (listModel.capacity() > 0)
			listModel.clear();
		if (file.exists())
		{
			try
			{
				scanner = new Scanner(file);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			if (scanner != null)
			{
				String str;
				int index;
				for (; scanner.hasNextLine(); listModel.insertElementAt(str, index))
				{
					index = list.getSelectedIndex();
					index++;
					str = scanner.nextLine();
				}

			}
		}
	}

	public void initConstraintsArray()
	{
		constraintnums = 0;
		constraints = new ConstraintRules();
	}

	public void SaveConstraint()
	{
		System.out.println("save");
		int index = 0;
		Vector vec = new Vector();
		Vector vecvalid = new Vector();
		constraintnums = listModel.getSize();
		exportblock.cons_line = "";
		for (index = 0; index < constraintnums; index++)
		{
			String content = (String)listModel.get(index);
			String split[] = content.split("  : ");
			constraints.setMstring(split[0]);
			constraints.doString();
			exportblock.setCfilename(split[1]);
			exportblock.setConstraint(constraints);
			if (!exportblock.doConstraint())
				vec.add(Integer.valueOf(index));
			else
				vecvalid.add(Integer.valueOf(index));
		}

		if (vec.size() > 0)
		{
			String str = "Constraint  ";
			int i = 0;
			int j = 0;
			int len;
			for (len = vec.size(); i < len; i++)
			{
				j = ((Integer)vec.get(i)).intValue();
				str = str + (String)listModel.get(j);
				str = str + "\n";
			}

			if (len == 1)
				str = str + "is invalid!";
			else
				str = str + "are invalid!";
			JOptionPane.showMessageDialog(null, str);
		}
		try
		{
			FileWriter writer = new FileWriter(constraintsFileName);
			String str = "";
			int i = 0;
			for (int len = vecvalid.size(); i < len; i++)
			{
				str = str + listModel.get(((Integer)vecvalid.get(i)).intValue());
				str = str + "\n";
			}

			writer.write(str);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		exportblock.close();
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
			list.getSelectedIndex();
	}

	public boolean doText()
	{
		ConstraintRules check = new ConstraintRules(constraint.getText());
		if (!check.doString())
		{
			return false;
		} else
		{
			constraint.setText(check.getString());
			return true;
		}
	}

	public ExportBlocks getExportblock()
	{
		return exportblock;
	}

	public void setExportblock(ExportBlocks exportblock)
	{
		this.exportblock = exportblock;
	}

	public String getConstraintsFileName()
	{
		return constraintsFileName;
	}

	public void setConstraintsFileName(String constraintsFileName)
	{
		this.constraintsFileName = constraintsFileName + ".lineinfo";
	}

	public String[] getCFiles()
	{
		return cFiles;
	}

	public void setCFiles(String files[])
	{
		cFiles = null;
		cFiles = files;
	}





}
