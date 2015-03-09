// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MainFrame.java

package src;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;

// Referenced classes of package src:
//			SplitPane, MessagePanel, CenterPane, CfgGraph, 
//			ConstraintPane, Shell, Tree, DisAssemblyPane, 
//			ExportBlocks, LoopInfo, file, InputDependent, 
//			ExampleFileFilter, loop

public class MainFrame extends JFrame
{
	public class recDialog extends JDialog
		implements PropertyChangeListener
	{

		JPanel panels[];
		JLabel labels[];
		JTextField fields[];
		JOptionPane option;
		int size;

		public void propertyChange(PropertyChangeEvent evt)
		{
			Object value = option.getValue();
			option.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return;
			if (((Integer)value).intValue() == 2)
			{
				setVisible(false);
				return;
			}
			boolean b = check();
			if (b)
			{
				setVisible(false);
				saveRecursive();
			} else
			{
				JOptionPane.showMessageDialog(this, "All targets are needed!", "Try Again", 0);
			}
		}

		public void saveRecursive()
		{
			int len = splitPane.disPane.exportblock.input.procs.length;
			FileWriter writer = null;
			String str = "";
			try
			{
				writer = new FileWriter(dirPath + "/" + dirName + ".recursive");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			str = str + len;
			str = str + "\n";
			for (int i = 0; i < len; i++)
			{
				String procname = splitPane.disPane.exportblock.input.procs[i].name;
				int j;
				for (j = 0; j < size; j++)
				{
					String recName = "<" + labels[j].getText() + ">";
					if (procname.compareTo(recName) == 0)
						break;
				}

				if (j < size)
					str = str + fields[j].getText();
				else
					str = str + "0";
				str = str + "\n";
			}

			try
			{
				writer.write(str);
				writer.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		public boolean check()
		{
			for (int i = 0; i < size; i++)
				if (fields[i].getText() == null || fields[i].getText().length() <= 0)
					return false;

			return true;
		}

		public recDialog(Vector vec)
		{
			size = vec.size();
			panels = new JPanel[size];
			labels = new JLabel[size];
			fields = new JTextField[size];
			for (int i = 0; i < size; i++)
			{
				labels[i] = new JLabel();
				labels[i].setText((String)vec.get(i));
				fields[i] = new JTextField();
				panels[i] = new JPanel();
				panels[i].setLayout(new GridLayout(1, 2));
				panels[i].add(labels[i]);
				panels[i].add(fields[i]);
			}

			Object object[] = {
				panels
			};
			option = new JOptionPane(((Object) (object)), 3, 2);
			setContentPane(option);
			option.addPropertyChangeListener(this);
		}
	}

	public class JrDialog extends JDialog
		implements PropertyChangeListener
	{

		JPanel panels[];
		JLabel adr[];
		JLabel inst[];
		JTextField target[];
		JOptionPane option;
		int njr;

		public void propertyChange(PropertyChangeEvent evt)
		{
			Object value = option.getValue();
			option.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return;
			if (((Integer)value).intValue() == 2)
			{
				setVisible(false);
				return;
			}
			boolean b = check();
			if (b)
			{
				FileWriter writer = null;
				try
				{
					writer = new FileWriter(dirPath + "/" + dirName + ".jtable");
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				String str = "";
				str = str + njr;
				str = str + "\n";
				for (int i = 0; i < njr; i++)
				{
					str = str + adr[i].getText();
					Scanner scan = new Scanner(target[i].getText());
					Vector vec = new Vector();
					for (; scan.hasNext(); vec.add(scan.next()));
					str = str + " ";
					str = str + vec.size();
					for (int j = 0; j < vec.size(); j++)
						str = str + " " + (String)vec.get(j);

					str = str + "\n";
				}

				try
				{
					writer.write(str);
					writer.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				setVisible(false);
				reload();
				bir_set = true;
			} else
			{
				JOptionPane.showMessageDialog(this, "All targets are needed!", "Try Again", 0);
			}
		}

		public boolean check()
		{
			for (int i = 0; i < njr; i++)
				if (target[i].getText() == null || target[i].getText().length() <= 0)
					return false;

			return true;
		}

		public JrDialog(int njump)
		{
			njr = 0;
			Scanner scan = null;
			njr = njump;
			try
			{
				File file = new File(dirPath + "/" + dirName + ".ir");
				scan = new Scanner(file);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			adr = new JLabel[njr];
			inst = new JLabel[njr];
			target = new JTextField[njr];
			panels = new JPanel[njr + 1];
			panels[0] = new JPanel();
			panels[0].setLayout(new GridLayout(1, 3));
			panels[0].add(new JLabel(" Address "));
			panels[0].add(new JLabel(" Instruction "));
			panels[0].add(new JLabel(" Jump Targets(address) "));
			for (int index = 0; scan.hasNext(); index++)
			{
				String str = scan.next();
				adr[index] = new JLabel();
				adr[index].setText(str);
				str = scan.next();
				str = str + " " + scan.next();
				inst[index] = new JLabel();
				inst[index].setText(str);
				target[index] = new JTextField();
				panels[index + 1] = new JPanel();
				panels[index + 1].setLayout(new GridLayout(1, 3));
				panels[index + 1].add(adr[index]);
				panels[index + 1].add(inst[index]);
				panels[index + 1].add(target[index]);
			}

			JLabel label = new JLabel("For multiple targets, please separate them by space!");
			Object object[] = {
				label, panels
			};
			option = new JOptionPane(((Object) (object)), 3, 2);
			setContentPane(option);
			option.addPropertyChangeListener(this);
		}
	}

	public class LoopDialog extends JDialog
		implements PropertyChangeListener
	{

		JPanel panels[];
		JLabel labels[];
		JTextField texts[];
		JComboBox comboxs[];
		String less[] = {
			"<", "=", "<="
		};
		int len;
		JOptionPane option;

		public void lastconfig()
		{
			File file = new File(dirPath + "/" + dirName + ".lpbound");
			if (!file.exists())
				return;
			try
			{
				Scanner scan = new Scanner(file);
				for (int i = 0; i < len; i++)
				{
					String index = scan.next();
					String lp = scan.next();
					comboxs[i].setSelectedIndex(Integer.parseInt(index));
					if (!lp.equals("-1"))
						texts[i].setText(lp);
				}

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public void setLoopName(int index, String name)
		{
			labels[index].setText(name);
		}

		public void propertyChange(PropertyChangeEvent evt)
		{
			Object value = option.getValue();
			option.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return;
			if (((Integer)value).intValue() == 2)
			{
				setVisible(false);
				return;
			}
			boolean b = check();
			if (b)
			{
				dependentloop = 0;
				int i = 0;
				int array[] = new int[len];
				String sign[] = new String[len];
				System.out.println("len " + len);
				for (; i < len; i++)
					if (texts[i].getText() == null || texts[i].getText().length() <= 0)
					{
						array[i] = -1;
						sign[i] = "";
					} else
					{
						array[i] = Integer.valueOf(texts[i].getText()).intValue();
						sign[i] = less[comboxs[i].getSelectedIndex()];
					}

				splitPane.disPane.exportblock.setLoopBoundConstraints(setindex, array, sign);
				try
				{
					FileWriter filewriter = new FileWriter(dirPath + "/" + dirName + ".lpbound");
					String lp = "";
					for (i = 0; i < len; i++)
					{
						lp = lp + comboxs[i].getSelectedIndex() + " " + array[i];
						lp = lp + "\n";
					}

					filewriter.write(lp);
					filewriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				setVisible(false);
			}
		}

		public boolean check()
		{
			return true;
		}

		public LoopDialog(int num)
		{
			int i = 0;
			panels = new JPanel[num];
			comboxs = new JComboBox[num];
			for (i = 0; i < num; i++)
			{
				panels[i] = new JPanel();
				comboxs[i] = new JComboBox(less);
			}

			labels = new JLabel[num];
			for (i = 0; i < num; i++)
				labels[i] = new JLabel();

			texts = new JTextField[num];
			for (i = 0; i < num; i++)
				texts[i] = new JTextField(6);

			for (i = 0; i < num; i++)
			{
				panels[i].setLayout(new GridLayout(1, 3));
				panels[i].add(labels[i]);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(1, 3));
				panel.add(Box.createHorizontalStrut(2));
				panel.add(comboxs[i]);
				panel.add(Box.createHorizontalStrut(2));
				panels[i].add(panel);
				panels[i].add(texts[i]);
			}

			len = num;
			JLabel title = new JLabel();
			JLabel jnotice1 = new JLabel();
			JLabel jnotice2 = new JLabel();
			String notice1 = "Notice : ** the loop bound constraints are relative to outer loop or procedure. For example, loop a is nested in loop b, ";
			String notice2 = "so the loop bound of a is relative to every invocation of loop b!";
			jnotice1.setText(notice1);
			jnotice2.setText(notice2);
			lastconfig();
			title.setText("For example : for loop from 1 to 10 = 100");
			Object object[] = {
				title, panels, jnotice1, jnotice2
			};
			option = new JOptionPane(((Object) (object)), 3, 2);
			setContentPane(option);
			option.addPropertyChangeListener(this);
		}
	}

	public class oldprocessorDialog extends JDialog
		implements PropertyChangeListener
	{

		JPanel panels[];
		JOptionPane option;
		String s_pipeline;
		JComboBox combox_super;
		String s_super[] = {
			"1", "2", "3", "4"
		};
		JComboBox combox_fetch;
		String s_fetch[] = {
			"2", "4", "8"
		};
		JComboBox combox_ruu;
		String s_ruu[] = {
			"2", "4", "8", "12", "16", "20", "24", "28", "32"
		};
		String s_title_super;
		String s_title_fetch;
		String s_title_ruu;
		String s_inorder;
		String s_outoforder;
		JLabel l_super;
		JLabel l_fetch;
		JLabel l_ruu;
		JPanel content;
		JRadioButton radio_inorder_enable;
		JRadioButton radio_outorder_enable;
		String s_cache;
		JTextField text_sets;
		JTextField text_block;
		JTextField text_mem;
		JComboBox combox_sets;
		String s_sets[] = {
			"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", 
			"1024"
		};
		JComboBox combox_bsize;
		String s_bsize[] = {
			"8", "16", "32", "64"
		};
		JComboBox combox_asso;
		String s_asso[] = {
			"1", "2", "4", "8"
		};
		String s_title_sets;
		String s_title_block;
		String s_title_asso;
		String s_title_mem;
		JLabel l_sets;
		JLabel l_block;
		JLabel l_asso;
		JLabel l_mem;
		JRadioButton radio_cache_enable;
		JRadioButton radio_cache_disable;
		boolean b_inorder;
		String s_branch;
		JComboBox combox_bht;
		String s_bht[] = {
			"16", "32", "128", "256", "512"
		};
		JComboBox combox_bhr;
		String s_bhr[] = {
			"1", "2", "3", "4"
		};
		JCheckBox check_bor;
		String s_title_bht;
		String s_title_bhr;
		String s_title_bor;
		JLabel l_bht;
		JLabel l_bhr;
		JLabel l_bor;
		JRadioButton radio_branch_enable;
		JRadioButton radio_branch_disable;
		boolean b_combox_sets;
		boolean b_combox_bsize;
		boolean b_combox_asso;
		boolean b_text_mem;
		boolean b_combox_bht;
		boolean b_combox_bhr;
		boolean b_check_bor;
		int size;
		int err;
		static final int FETCH_SMALL_THAN_SUPER = 100;
		static final int LARGE_MEMORY_LATENCY = 200;
		static final int MAX_MEMORY_LATENCY = 50;
		String s_enable;
		String s_disable;
		String Err_SMALL;
		String Err_MEM;
		int cache_nsets;
		int cache_bsize;
		int mem_lat;

		public void init_pipeline()
		{
			JLabel label = new JLabel(s_pipeline);
			l_super = new JLabel(s_title_super);
			l_fetch = new JLabel(s_title_fetch);
			l_ruu = new JLabel(s_title_ruu);
			combox_super = new JComboBox(s_super);
			combox_fetch = new JComboBox(s_fetch);
			combox_ruu = new JComboBox(s_ruu);
			radio_inorder_enable = new JRadioButton();
			radio_outorder_enable = new JRadioButton();
			panels[0].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_inorder);
			panel_en.add(radio_inorder_enable);
			panel_en.add(label_en);
			panels[0].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_outoforder);
			panel_dis.add(radio_outorder_enable);
			panel_dis.add(label_dis);
			panels[0].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_inorder_enable);
			group.add(radio_outorder_enable);
			panels[1].add(Box.createHorizontalStrut(10));
			panels[1].add(l_super);
			panels[1].add(combox_super);
			panels[2].add(Box.createHorizontalStrut(10));
			panels[2].add(l_fetch);
			panels[2].add(combox_fetch);
			panels[3].add(Box.createHorizontalStrut(10));
			panels[3].add(l_ruu);
			panels[3].add(combox_ruu);
		}

		public void init_cache()
		{
			JLabel label = new JLabel(s_cache);
			l_sets = new JLabel(s_title_sets);
			l_block = new JLabel(s_title_block);
			l_asso = new JLabel(s_title_asso);
			l_mem = new JLabel(s_title_mem);
			combox_sets = new JComboBox(s_sets);
			combox_bsize = new JComboBox(s_bsize);
			combox_asso = new JComboBox(s_asso);
			text_mem = new JTextField(10);
			radio_cache_enable = new JRadioButton();
			radio_cache_disable = new JRadioButton();
			panels[4].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_enable);
			panel_en.add(radio_cache_enable);
			panel_en.add(label_en);
			panels[4].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_disable);
			panel_dis.add(radio_cache_disable);
			panel_dis.add(label_dis);
			panels[4].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_cache_enable);
			group.add(radio_cache_disable);
			panels[5].add(Box.createHorizontalStrut(10));
			panels[5].add(l_sets);
			panels[5].add(combox_sets);
			panels[6].add(Box.createHorizontalStrut(10));
			panels[6].add(l_block);
			panels[6].add(combox_bsize);
			panels[7].add(Box.createHorizontalStrut(10));
			panels[7].add(l_asso);
			panels[7].add(combox_asso);
			panels[8].add(Box.createHorizontalStrut(10));
			panels[8].add(l_mem);
			panels[8].add(text_mem);
		}

		public void init_branch()
		{
			JLabel label = new JLabel(s_branch);
			l_bht = new JLabel(s_title_bht);
			l_bhr = new JLabel(s_title_bhr);
			l_bor = new JLabel(s_title_bor);
			combox_bht = new JComboBox(s_bht);
			combox_bhr = new JComboBox(s_bhr);
			check_bor = new JCheckBox();
			radio_branch_enable = new JRadioButton();
			radio_branch_disable = new JRadioButton();
			panels[9].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_enable);
			panel_en.add(radio_branch_enable);
			panel_en.add(label_en);
			panels[9].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_disable);
			panel_dis.add(radio_branch_disable);
			panel_dis.add(label_dis);
			panels[9].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_branch_enable);
			group.add(radio_branch_disable);
			panels[10].add(Box.createHorizontalStrut(10));
			panels[10].add(l_bht);
			panels[10].add(combox_bht);
			panels[11].add(Box.createHorizontalStrut(10));
			panels[11].add(l_bhr);
			panels[11].add(combox_bhr);
			panels[12].add(Box.createHorizontalStrut(10));
			panels[12].add(l_bor);
			panels[12].add(check_bor);
		}

		public void init()
		{
			b_inorder = false;
			b_combox_sets = false;
			b_combox_bsize = false;
			b_combox_asso = false;
			b_text_mem = false;
			b_combox_bht = false;
			b_combox_bhr = false;
			b_check_bor = false;
			combox_sets.setEnabled(b_combox_sets);
			combox_bsize.setEnabled(b_combox_bsize);
			combox_asso.setEnabled(b_combox_asso);
			text_mem.setText((new StringBuffer(String.valueOf(mem_lat))).toString());
			text_mem.setEnabled(b_text_mem);
			combox_bht.setEnabled(b_combox_bht);
			combox_bhr.setEnabled(b_combox_bhr);
			check_bor.setEnabled(b_check_bor);
			radio_outorder_enable.setSelected(true);
			radio_cache_disable.setSelected(true);
			radio_branch_disable.setSelected(true);
		}

		public void radiopipelineAction(boolean b)
		{
			b_inorder = b;
			combox_super.setEnabled(!b_inorder);
		}

		public void radiobranchAction(boolean b)
		{
			b_combox_bht = b;
			b_combox_bhr = b;
			b_check_bor = b;
			combox_bht.setEnabled(b_combox_bht);
			combox_bhr.setEnabled(b_combox_bhr);
			check_bor.setEnabled(b_check_bor);
		}

		public void radiocacheAction(boolean b)
		{
			b_combox_sets = b;
			b_combox_bsize = b;
			b_combox_asso = b;
			b_text_mem = b;
			combox_sets.setEnabled(b_combox_sets);
			combox_bsize.setEnabled(b_combox_bsize);
			combox_asso.setEnabled(b_combox_asso);
			text_mem.setEnabled(b_text_mem);
		}

		public void propertyChange(PropertyChangeEvent arg0)
		{
			Object value = option.getValue();
			option.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return;
			if (((Integer)value).intValue() == 2)
			{
				setVisible(false);
				return;
			}
			if (propertycheck())
			{
				String config = "";
				if (radio_cache_disable.isSelected())
				{
					config = config + " -cache:il1 none";
				} else
				{
					config = config + " -cache:il1 il1";
					config = config + ":" + s_sets[combox_sets.getSelectedIndex()];
					config = config + ":" + s_bsize[combox_bsize.getSelectedIndex()];
					config = config + ":" + s_asso[combox_asso.getSelectedIndex()];
					config = config + ":l";
					config = config + " -mem:lat ";
					config = config + text_mem.getText();
					config = config + " 2";
				}
				if (radio_branch_disable.isSelected())
				{
					config = config + " -bpred perfect";
				} else
				{
					config = config + " -bpred 2lev";
					config = config + " -bpred:2lev ";
					config = config + " 1";
					config = config + " " + s_bht[combox_bht.getSelectedIndex()];
					config = config + " " + s_bhr[combox_bhr.getSelectedIndex()];
					if (check_bor.isSelected())
						config = config + " 1";
					else
						config = config + " 0";
				}
				if (radio_outorder_enable.isSelected())
				{
					config = config + " -decode:width " + s_super[combox_super.getSelectedIndex()];
					config = config + " -issue:width " + s_super[combox_super.getSelectedIndex()];
					config = config + " -commit:width " + s_super[combox_super.getSelectedIndex()];
				} else
				{
					config = config + " -issue:inorder true ";
				}
				config = config + " -fetch:ifqsize " + s_fetch[combox_fetch.getSelectedIndex()];
				config = config + " -ruu:size " + s_ruu[combox_ruu.getSelectedIndex()];
				try
				{
					FileWriter writer = new FileWriter(MainFrame.currentpath + "/est/processor.opt");
					writer.write(config);
					writer.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				setVisible(false);
			} else
			{
				String serr = "";
				switch (err)
				{
				case 100: // 'd'
					serr = Err_SMALL;
					break;

				case 200: 
					serr = Err_MEM;
					break;
				}
				JOptionPane.showMessageDialog(null, serr);
			}
		}

		public boolean propertycheck()
		{
			int a;
			if (radio_cache_enable.isSelected())
			{
				a = Integer.valueOf(text_mem.getText()).intValue();
				if (a > 50)
				{
					err = 200;
					return false;
				}
			}
			a = Integer.valueOf(s_super[combox_super.getSelectedIndex()]).intValue();
			int b = Integer.valueOf(s_fetch[combox_fetch.getSelectedIndex()]).intValue();
			if (a > b)
			{
				err = 100;
				return false;
			} else
			{
				return true;
			}
		}

		public oldprocessorDialog()
		{
			s_pipeline = "Pipeline Parameters:";
			s_title_super = "Superscalarity :";
			s_title_fetch = "Instruction Fetch Queue Size :";
			s_title_ruu = "Reorder Buffer Size :";
			s_inorder = "In-order";
			s_outoforder = "Out-of-order";
			s_cache = "Instruction Cache Parameters:";
			s_title_sets = "Number of Sets :";
			s_title_block = "Block Size :";
			s_title_asso = "Cache Associativity :";
			s_title_mem = "Main Memory Access Latency: ";
			s_branch = "Branch Prediction Parameters:";
			s_title_bht = "Branch History Table(BHT) Size: ";
			s_title_bhr = "Branch History Register(BHR) Width: ";
			s_title_bor = "BHR XORed with Address: ";
			size = 13;
			s_enable = "Enable";
			s_disable = "Disable";
			Err_SMALL = "Instruction Fetch Queue Size should be larger than Superscalarity";
			Err_MEM = "Memory latency can not be larger than 50";
			cache_nsets = 32;
			cache_bsize = 32;
			mem_lat = 30;
			panels = new JPanel[size];
			for (int index = 0; index < size; index++)
			{
				panels[index] = new JPanel();
				panels[index].setLayout(new GridLayout(1, 3));
			}

			init_pipeline();
			init_cache();
			init_branch();
			Object object[] = {
				panels
			};
			option = new JOptionPane(((Object) (object)), 3, 2);
			setContentPane(option);
			option.addPropertyChangeListener(this);
			init();
			radio_inorder_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiopipelineAction(true);
				}

			});
			radio_outorder_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiopipelineAction(false);
				}

			});
			radio_cache_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiocacheAction(true);
				}

			});
			radio_cache_disable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiocacheAction(false);
				}

			});
			radio_branch_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiobranchAction(true);
				}

			});
			radio_branch_disable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiobranchAction(false);
				}

			});
		}
	}

	public class processorDialog extends JDialog
		implements PropertyChangeListener
	{

		JPanel panels[];
		JOptionPane option;
		String s_pipeline;
		JComboBox combox_super;
		String s_super[] = {
			"1", "2", "3", "4"
		};
		JComboBox combox_fetch;
		String s_fetch[] = {
			"2", "4", "8"
		};
		JComboBox combox_ruu;
		String s_ruu[] = {
			"2", "4", "8", "12", "16", "20", "24", "28", "32"
		};
		String s_title_super;
		String s_title_fetch;
		String s_title_ruu;
		String s_inorder;
		String s_outoforder;
		JLabel l_super;
		JLabel l_fetch;
		JLabel l_ruu;
		JPanel content;
		JRadioButton radio_inorder_enable;
		JRadioButton radio_outorder_enable;
		String s_cache;
		JTextField text_sets;
		JTextField text_block;
		JTextField text_mem;
		JComboBox combox_sets;
		String s_sets[] = {
			"1", "2", "4", "8", "16", "32", "64", "128", "256", "512", 
			"1024"
		};
		JComboBox combox_bsize;
		String s_bsize[] = {
			"8", "16", "32", "64"
		};
		JComboBox combox_asso;
		String s_asso[] = {
			"1", "2", "4", "8"
		};
		String s_title_sets;
		String s_title_block;
		String s_title_asso;
		String s_title_mem;
		JLabel l_sets;
		JLabel l_block;
		JLabel l_asso;
		JLabel l_mem;
		JRadioButton radio_cache_enable;
		JRadioButton radio_cache_disable;
		boolean b_inorder;
		String s_branch;
		JComboBox combox_bht;
		String s_bht[] = {
			"16", "32", "64", "128", "256", "512"
		};
		JComboBox combox_bhr;
		String s_bhr[] = {
			"1", "2", "3", "4"
		};
		JCheckBox check_bor;
		String s_title_bht;
		String s_title_bhr;
		String s_title_bor;
		JLabel l_bht;
		JLabel l_bhr;
		JLabel l_bor;
		JRadioButton radio_branch_enable;
		JRadioButton radio_branch_disable;
		boolean b_combox_sets;
		boolean b_combox_bsize;
		boolean b_combox_asso;
		boolean b_text_mem;
		boolean b_combox_bht;
		boolean b_combox_bhr;
		boolean b_check_bor;
		int size;
		int err;
		static final int FETCH_SMALL_THAN_SUPER = 100;
		static final int LARGE_MEMORY_LATENCY = 200;
		static final int MAX_MEMORY_LATENCY = 50;
		String s_enable;
		String s_disable;
		String Err_SMALL;
		String Err_MEM;
		int cache_nsets;
		int cache_bsize;
		int mem_lat;

		public void init_pipeline()
		{
			JLabel label = new JLabel(s_pipeline);
			l_super = new JLabel(s_title_super);
			l_fetch = new JLabel(s_title_fetch);
			l_ruu = new JLabel(s_title_ruu);
			combox_super = new JComboBox(s_super);
			combox_fetch = new JComboBox(s_fetch);
			combox_ruu = new JComboBox(s_ruu);
			radio_inorder_enable = new JRadioButton();
			radio_outorder_enable = new JRadioButton();
			panels[0].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_inorder);
			panel_en.add(radio_inorder_enable);
			panel_en.add(label_en);
			panels[0].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_outoforder);
			panel_dis.add(radio_outorder_enable);
			panel_dis.add(label_dis);
			panels[0].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_inorder_enable);
			group.add(radio_outorder_enable);
			panels[1].add(Box.createHorizontalStrut(10));
			panels[1].add(l_super);
			panels[1].add(combox_super);
			panels[2].add(Box.createHorizontalStrut(10));
			panels[2].add(l_fetch);
			panels[2].add(combox_fetch);
			panels[3].add(Box.createHorizontalStrut(10));
			panels[3].add(l_ruu);
			panels[3].add(combox_ruu);
		}

		public void init_cache()
		{
			JLabel label = new JLabel(s_cache);
			l_sets = new JLabel(s_title_sets);
			l_block = new JLabel(s_title_block);
			l_asso = new JLabel(s_title_asso);
			l_mem = new JLabel(s_title_mem);
			combox_sets = new JComboBox(s_sets);
			combox_bsize = new JComboBox(s_bsize);
			combox_asso = new JComboBox(s_asso);
			text_mem = new JTextField(10);
			radio_cache_enable = new JRadioButton();
			radio_cache_disable = new JRadioButton();
			panels[4].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_enable);
			panel_en.add(radio_cache_enable);
			panel_en.add(label_en);
			panels[4].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_disable);
			panel_dis.add(radio_cache_disable);
			panel_dis.add(label_dis);
			panels[4].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_cache_enable);
			group.add(radio_cache_disable);
			panels[5].add(Box.createHorizontalStrut(10));
			panels[5].add(l_sets);
			panels[5].add(combox_sets);
			panels[6].add(Box.createHorizontalStrut(10));
			panels[6].add(l_block);
			panels[6].add(combox_bsize);
			panels[7].add(Box.createHorizontalStrut(10));
			panels[7].add(l_asso);
			panels[7].add(combox_asso);
			panels[8].add(Box.createHorizontalStrut(10));
			panels[8].add(l_mem);
			panels[8].add(text_mem);
		}

		public void init_branch()
		{
			JLabel label = new JLabel(s_branch);
			l_bht = new JLabel(s_title_bht);
			l_bhr = new JLabel(s_title_bhr);
			l_bor = new JLabel(s_title_bor);
			combox_bht = new JComboBox(s_bht);
			combox_bhr = new JComboBox(s_bhr);
			check_bor = new JCheckBox();
			radio_branch_enable = new JRadioButton();
			radio_branch_disable = new JRadioButton();
			panels[9].add(label);
			JPanel panel_en = new JPanel();
			JLabel label_en = new JLabel(s_enable);
			panel_en.add(radio_branch_enable);
			panel_en.add(label_en);
			panels[9].add(panel_en);
			JPanel panel_dis = new JPanel();
			JLabel label_dis = new JLabel(s_disable);
			panel_dis.add(radio_branch_disable);
			panel_dis.add(label_dis);
			panels[9].add(panel_dis);
			ButtonGroup group = new ButtonGroup();
			group.add(radio_branch_enable);
			group.add(radio_branch_disable);
			panels[10].add(Box.createHorizontalStrut(10));
			panels[10].add(l_bht);
			panels[10].add(combox_bht);
			panels[11].add(Box.createHorizontalStrut(10));
			panels[11].add(l_bhr);
			panels[11].add(combox_bhr);
			panels[12].add(Box.createHorizontalStrut(10));
			panels[12].add(l_bor);
			panels[12].add(check_bor);
		}

		public void init()
		{
			b_inorder = false;
			b_combox_sets = false;
			b_combox_bsize = false;
			b_combox_asso = false;
			b_text_mem = false;
			b_combox_bht = false;
			b_combox_bhr = false;
			b_check_bor = false;
			combox_sets.setEnabled(b_combox_sets);
			combox_bsize.setEnabled(b_combox_bsize);
			combox_asso.setEnabled(b_combox_asso);
			text_mem.setText((new StringBuffer(String.valueOf(mem_lat))).toString());
			text_mem.setEnabled(b_text_mem);
			combox_bht.setEnabled(b_combox_bht);
			combox_bhr.setEnabled(b_combox_bhr);
			check_bor.setEnabled(b_check_bor);
			radio_outorder_enable.setSelected(true);
			radio_cache_disable.setSelected(true);
			radio_branch_disable.setSelected(true);
		}

		public void setToDefault()
		{
			b_inorder = false;
			b_combox_sets = true;
			b_combox_bsize = true;
			b_combox_asso = true;
			b_text_mem = true;
			b_combox_bht = true;
			b_combox_bhr = true;
			b_check_bor = true;
			System.out.println("set to default");
			radio_outorder_enable.setSelected(true);
			combox_super.setSelectedIndex(0);
			combox_fetch.setSelectedIndex(1);
			combox_ruu.setSelectedIndex(2);
			radio_cache_enable.setSelected(true);
			combox_sets.setSelectedIndex(4);
			combox_bsize.setSelectedIndex(2);
			combox_asso.setSelectedIndex(1);
			text_mem.setText((new StringBuffer(String.valueOf(mem_lat))).toString());
			text_mem.setEnabled(b_text_mem);
			radio_branch_enable.setSelected(true);
			combox_bht.setSelectedIndex(3);
			combox_bhr.setSelectedIndex(1);
			check_bor.setSelected(true);
		}

		int getCacheIndex(int type, int num)
		{
			int index = 0;
			switch (type)
			{
			default:
				break;

			case 0: // '\0'
				while (num > 1) 
				{
					num /= 2;
					index++;
				}
				break;

			case 1: // '\001'
				while (num > 8) 
				{
					num /= 2;
					index++;
				}
				break;

			case 2: // '\002'
				while (num > 1) 
				{
					num /= 2;
					index++;
				}
				break;
			}
			return index;
		}

		int getBranchIndex(int type, int num)
		{
			int index = 0;
			switch (type)
			{
			default:
				break;

			case 0: // '\0'
				while (num > 16) 
				{
					num /= 2;
					index++;
				}
				break;

			case 1: // '\001'
				while (num > 1) 
				{
					num--;
					index++;
				}
				break;
			}
			return index;
		}

		int getPipelineIndex(int type, int num)
		{
			int index = 0;
			switch (type)
			{
			case 0: // '\0'
				while (num > 1) 
				{
					num--;
					index++;
				}
				break;

			case 1: // '\001'
				while (num > 2) 
				{
					num /= 2;
					index++;
				}
				break;
			}
			if (type == 2)
				switch (num)
				{
				case 2: // '\002'
					index = 0;
					break;

				case 4: // '\004'
					index = 1;
					break;

				case 8: // '\b'
					index = 2;
					break;

				case 12: // '\f'
					index = 3;
					break;

				case 16: // '\020'
					index = 4;
					break;

				case 20: // '\024'
					index = 5;
					break;

				case 24: // '\030'
					index = 6;
					break;

				case 28: // '\034'
					index = 7;
					break;

				case 32: // ' '
					index = 8;
					break;
				}
			return index;
		}

		public void readConfig()
		{
			File file = new File(MainFrame.currentpath + "/est/processor.opt");
			if (file.exists())
				try
				{
					Scanner scan = new Scanner(file);
					scan.next();
					String str = scan.next();
					b_combox_sets = false;
					b_combox_bsize = false;
					b_combox_asso = false;
					b_text_mem = false;
					int index;
					int value;
					if (str.equals("none"))
					{
						radio_cache_disable.setSelected(true);
						combox_sets.setEnabled(b_combox_sets);
						combox_bsize.setEnabled(b_combox_bsize);
						combox_asso.setEnabled(b_combox_asso);
						text_mem.setText((new StringBuffer(String.valueOf(mem_lat))).toString());
						text_mem.setEnabled(b_text_mem);
					} else
					{
						Scanner word = new Scanner(str);
						word.useDelimiter(":");
						word.next();
						radio_cache_enable.setSelected(true);
						b_combox_sets = true;
						b_combox_bsize = true;
						b_combox_asso = true;
						b_text_mem = true;
						str = word.next();
						value = Integer.parseInt(str);
						index = getCacheIndex(0, value);
						combox_sets.setSelectedIndex(index);
						str = word.next();
						value = Integer.parseInt(str);
						index = getCacheIndex(1, value);
						combox_bsize.setSelectedIndex(index);
						str = word.next();
						value = Integer.parseInt(str);
						index = getCacheIndex(2, value);
						combox_asso.setSelectedIndex(index);
						scan.next();
						str = scan.next();
						mem_lat = Integer.parseInt(str);
						text_mem.setText((new StringBuffer(String.valueOf(mem_lat))).toString());
						text_mem.setEnabled(b_text_mem);
						scan.next();
					}
					scan.next();
					str = scan.next();
					if (str.equals("perfect"))
					{
						radio_branch_disable.setSelected(true);
						b_combox_bht = false;
						b_combox_bhr = false;
						b_check_bor = false;
						combox_bht.setEnabled(b_combox_bht);
						combox_bhr.setEnabled(b_combox_bhr);
						check_bor.setEnabled(b_check_bor);
					} else
					{
						radio_branch_enable.setSelected(true);
						b_combox_bht = true;
						b_combox_bhr = true;
						scan.next();
						scan.next();
						str = scan.next();
						value = Integer.parseInt(str);
						index = getBranchIndex(0, value);
						combox_bht.setSelectedIndex(index);
						str = scan.next();
						value = Integer.parseInt(str);
						index = getBranchIndex(1, value);
						combox_bhr.setSelectedIndex(index);
						str = scan.next();
						if (str.equals("1"))
							b_check_bor = true;
						else
							b_check_bor = false;
						check_bor.setSelected(b_check_bor);
					}
					str = scan.next();
					if (str.equals("-issue:inorder"))
					{
						b_inorder = true;
						radio_inorder_enable.setSelected(true);
						combox_super.setEnabled(!b_inorder);
						scan.next();
					} else
					{
						radio_outorder_enable.setSelected(true);
						str = scan.next();
						value = Integer.parseInt(str);
						index = getPipelineIndex(0, value);
						combox_super.setSelectedIndex(index);
						scan.next();
						scan.next();
						scan.next();
						scan.next();
					}
					scan.next();
					str = scan.next();
					value = Integer.parseInt(str);
					index = getPipelineIndex(1, value);
					combox_fetch.setSelectedIndex(index);
					scan.next();
					str = scan.next();
					value = Integer.parseInt(str);
					index = getPipelineIndex(2, value);
					combox_ruu.setSelectedIndex(index);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
			else
				setToDefault();
		}

		public void radiopipelineAction(boolean b)
		{
			b_inorder = b;
			combox_super.setEnabled(!b_inorder);
		}

		public void radiobranchAction(boolean b)
		{
			b_combox_bht = b;
			b_combox_bhr = b;
			b_check_bor = b;
			combox_bht.setEnabled(b_combox_bht);
			combox_bhr.setEnabled(b_combox_bhr);
			check_bor.setEnabled(b_check_bor);
		}

		public void radiocacheAction(boolean b)
		{
			b_combox_sets = b;
			b_combox_bsize = b;
			b_combox_asso = b;
			b_text_mem = b;
			combox_sets.setEnabled(b_combox_sets);
			combox_bsize.setEnabled(b_combox_bsize);
			combox_asso.setEnabled(b_combox_asso);
			text_mem.setEnabled(b_text_mem);
		}

		public void propertyChange(PropertyChangeEvent arg0)
		{
			Object value = option.getValue();
			option.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if (value == JOptionPane.UNINITIALIZED_VALUE)
				return;
			if (((Integer)value).intValue() == 2)
			{
				setVisible(false);
				return;
			}
			if (propertycheck())
			{
				String config = "";
				if (radio_cache_disable.isSelected())
				{
					config = config + " -cache:il1 none";
				} else
				{
					config = config + " -cache:il1 il1";
					config = config + ":" + s_sets[combox_sets.getSelectedIndex()];
					config = config + ":" + s_bsize[combox_bsize.getSelectedIndex()];
					config = config + ":" + s_asso[combox_asso.getSelectedIndex()];
					config = config + ":l";
					config = config + " -mem:lat ";
					config = config + text_mem.getText();
					config = config + " 2";
				}
				if (radio_branch_disable.isSelected())
				{
					config = config + " -bpred perfect";
				} else
				{
					config = config + " -bpred 2lev";
					config = config + " -bpred:2lev ";
					config = config + " 1";
					config = config + " " + s_bht[combox_bht.getSelectedIndex()];
					config = config + " " + s_bhr[combox_bhr.getSelectedIndex()];
					if (check_bor.isSelected())
						config = config + " 1";
					else
						config = config + " 0";
				}
				if (radio_outorder_enable.isSelected())
				{
					config = config + " -decode:width " + s_super[combox_super.getSelectedIndex()];
					config = config + " -issue:width " + s_super[combox_super.getSelectedIndex()];
					config = config + " -commit:width " + s_super[combox_super.getSelectedIndex()];
				} else
				{
					config = config + " -issue:inorder true ";
				}
				config = config + " -fetch:ifqsize " + s_fetch[combox_fetch.getSelectedIndex()];
				config = config + " -ruu:size " + s_ruu[combox_ruu.getSelectedIndex()];
				try
				{
					FileWriter writer = new FileWriter(MainFrame.currentpath + "/est/processor.opt");
					writer.write(config);
					writer.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				setVisible(false);
			} else
			{
				String serr = "";
				switch (err)
				{
				case 100: // 'd'
					serr = Err_SMALL;
					break;

				case 200: 
					serr = Err_MEM;
					break;
				}
				JOptionPane.showMessageDialog(null, serr);
			}
		}

		public boolean propertycheck()
		{
			int a;
			if (radio_cache_enable.isSelected())
			{
				a = Integer.valueOf(text_mem.getText()).intValue();
				if (a > 50)
				{
					err = 200;
					return false;
				}
			}
			a = Integer.valueOf(s_super[combox_super.getSelectedIndex()]).intValue();
			int b = Integer.valueOf(s_fetch[combox_fetch.getSelectedIndex()]).intValue();
			if (a > b)
			{
				err = 100;
				return false;
			} else
			{
				return true;
			}
		}

		public processorDialog()
		{
			s_pipeline = "Pipeline Parameters:";
			s_title_super = "Superscalarity :";
			s_title_fetch = "Instruction Fetch Queue Size :";
			s_title_ruu = "Reorder Buffer Size :";
			s_inorder = "In-order";
			s_outoforder = "Out-of-order";
			s_cache = "Instruction Cache Parameters:";
			s_title_sets = "Number of Sets :";
			s_title_block = "Block Size :";
			s_title_asso = "Cache Associativity :";
			s_title_mem = "Main Memory Access Latency: ";
			s_branch = "Branch Prediction Parameters:";
			s_title_bht = "Branch History Table(BHT) Size: ";
			s_title_bhr = "Branch History Register(BHR) Width: ";
			s_title_bor = "BHR XORed with Address: ";
			size = 13;
			s_enable = "Enable";
			s_disable = "Disable";
			Err_SMALL = "Instruction Fetch Queue Size should be larger than Superscalarity";
			Err_MEM = "Memory latency can not be larger than 50";
			cache_nsets = 32;
			cache_bsize = 32;
			mem_lat = 30;
			panels = new JPanel[size];
			for (int index = 0; index < size; index++)
			{
				panels[index] = new JPanel();
				panels[index].setLayout(new GridLayout(1, 3));
			}

			init_pipeline();
			init_cache();
			init_branch();
			Object object[] = {
				panels
			};
			option = new JOptionPane(((Object) (object)), 3, 2);
			setContentPane(option);
			option.addPropertyChangeListener(this);
			readConfig();
			radio_inorder_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiopipelineAction(true);
				}

			});
			radio_outorder_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiopipelineAction(false);
				}

			});
			radio_cache_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiocacheAction(true);
				}

			});
			radio_cache_disable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiocacheAction(false);
				}

			});
			radio_branch_enable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiobranchAction(true);
				}

			});
			radio_branch_disable.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt)
				{
					radiobranchAction(false);
				}

			});
		}
	}


	public SplitPane splitPane;
	public MessagePanel messagePane;
	public String openfileName;
	public String optfileName;
	public static String currentpath;
	public Shell shell;
	public ConstraintPane pane;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JMenu optionMenu;
	private JMenu runMenu;
	private JMenuItem openfileItem;
	private JMenuItem savefileItem;
	private JMenuItem aboutItem;
	private JMenuItem configItem;
	private JMenuItem lpsolveItem;
	private JMenuItem simplesimItem;
	private JMenuItem loopconstraintItem;
	private JMenuItem analyzeItem;
	private JMenuItem dumpItem;
	private JMenuItem processorItem;
	private JMenuItem simulateItem;
	private JMenuItem otherconstraintsItem;
	private JMenuItem irjumpItem;
	private JMenuItem recursiveItem;
	private JMenuItem exitItem;
	private JCheckBoxMenuItem stopItem;
	private static String s_title = "chronos";
	private static String s_openfile = "Open File ...";
	private static String s_about = "About";
	private static String s_save = "Save";
	private static String s_analyze = "Estimate";
	private static String s_processor = "Processor configuration";
	private static String s_simulate = "Simulate";
	private static String s_dump = "Dump Processor Configurations";
	private static String s_info = "Welcome to chronos!";
	private static String s_stop = "Stop Help Text";
	private static String s_set = "Options";
	private static String s_config = "Simplescalar-gcc bin directory";
	private static String s_solve = "ILP-solver directory";
	private static String s_simplesim = "Simplesim-3.0 directory";
	private static String s_cplex = "Cplex directory";
	private static String s_loop_constraint = "Loop bound constraints";
	private static String s_constraint_others = "Other constraints";
	private static String s_ir_jump = "Indirect jump targets";
	private static String s_recursion = "Recursion bound";
	private static String s_exit = "Exit";
	private String dirPath;
	private String dirName;
	private int loopindex[];
	private int setindex[];
	private int dependentloop;
	private int nloop;
	private loop loops[];
	private file files[];
	private int nfile;
	public String CFiles[];
	static final String err = "error";
	static final String err_SIM = "Invalid Simulation!";
	private boolean bir_jump;
	private int njump;
	private boolean bir_set;

	public MainFrame()
	{
		bir_jump = false;
		njump = 0;
		bir_set = false;
		splitPane = new SplitPane();
		messagePane = new MessagePanel();
		setDefaultCloseOperation(3);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPane.getSplitPane(), "Center");
		getContentPane().add(messagePane, "South");
		setTitle(s_title);
		initComponents();
		Dimension minimumSize = new Dimension(1200, 600);
		setMinimumSize(minimumSize);
		pack();
		splitPane.center.cfggraph.setMainframe(this);
	}

	public void initComponents()
	{
		pane = new ConstraintPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		helpMenu = new JMenu("Help");
		optionMenu = new JMenu("Option");
		runMenu = new JMenu("Run");
		openfileItem = new JMenuItem();
		savefileItem = new JMenuItem();
		aboutItem = new JMenuItem();
		configItem = new JMenuItem();
		lpsolveItem = new JMenuItem();
		simplesimItem = new JMenuItem();
		loopconstraintItem = new JMenuItem();
		analyzeItem = new JMenuItem();
		dumpItem = new JMenuItem();
		processorItem = new JMenuItem();
		otherconstraintsItem = new JMenuItem();
		simulateItem = new JMenuItem();
		stopItem = new JCheckBoxMenuItem(s_stop);
		irjumpItem = new JMenuItem();
		recursiveItem = new JMenuItem();
		exitItem = new JMenuItem();
		openfileItem.setText(s_openfile);
		savefileItem.setText(s_save);
		aboutItem.setText(s_about);
		configItem.setText(s_config);
		lpsolveItem.setText(s_solve);
		simplesimItem.setText(s_simplesim);
		loopconstraintItem.setText(s_loop_constraint);
		otherconstraintsItem.setText(s_constraint_others);
		analyzeItem.setText(s_analyze);
		dumpItem.setText(s_dump);
		processorItem.setText(s_processor);
		simulateItem.setText(s_simulate);
		irjumpItem.setText(s_ir_jump);
		recursiveItem.setText(s_recursion);
		exitItem.setText(s_exit);
		fileMenu.add(openfileItem);
		fileMenu.add(exitItem);
		editMenu.add(stopItem);
		optionMenu.add(configItem);
		optionMenu.add(lpsolveItem);
		optionMenu.add(simplesimItem);
		optionMenu.add(loopconstraintItem);
		optionMenu.add(irjumpItem);
		optionMenu.add(recursiveItem);
		optionMenu.add(otherconstraintsItem);
		runMenu.add(analyzeItem);
		runMenu.add(simulateItem);
		runMenu.add(processorItem);
		helpMenu.add(aboutItem);
		configItem.setEnabled(true);
		lpsolveItem.setEnabled(true);
		simplesimItem.setEnabled(true);
		loopconstraintItem.setEnabled(false);
		irjumpItem.setEnabled(false);
		recursiveItem.setEnabled(false);
		otherconstraintsItem.setEnabled(false);
		analyzeItem.setEnabled(false);
		dumpItem.setEnabled(true);
		processorItem.setEnabled(false);
		stopItem.setEnabled(false);
		dumpItem.setEnabled(false);
		simulateItem.setEnabled(false);
		exitItem.setEnabled(true);
		menuBar.add(fileMenu);
		menuBar.add(optionMenu);
		menuBar.add(runMenu);
		menuBar.add(helpMenu);
		openfileItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				openDirectory();
			}

		});
		savefileItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				savefileAction(evt);
			}

		});
		aboutItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				aboutAction(evt);
			}

		});
		stopItem.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemevent)
			{
			}

		});
		configItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				configAction(evt);
			}

		});
		loopconstraintItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				loopConstraintAction();
			}

		});
		lpsolveItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				setSolveDir();
			}

		});
		simplesimItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				setSimplesimDir();
			}

		});
		otherconstraintsItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				otherconstraintsAction(evt);
			}

		});
		analyzeItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				analyzeAction(evt);
			}

		});
		dumpItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				dumpAction(evt);
			}

		});
		processorItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				processorAction(evt);
			}

		});
		simulateItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				simulateAction();
			}

		});
		irjumpItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				irjumpAction();
			}

		});
		recursiveItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				recursiveAction();
			}

		});
		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt)
			{
				exitAction();
			}

		});
		setJMenuBar(menuBar);
		shell = new Shell();
		setcurrentpath();
		shell.setRun_dir(currentpath);
		System.out.println("Current Path:" + currentpath);
		splitPane.center.tree.setPane(pane);
	}

	public void exitAction()
	{
		Runtime.getRuntime().exit(0);
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

		CFiles = new String[num];
		i = 0;
		int j = 0;
		for (; i < children.length; i++)
			if (isCFile(children[i]))
			{
				System.out.println(children[i]);
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
				if (CFiles[i].compareToIgnoreCase(CFiles[j]) > 0)
				{
					String temp = CFiles[i];
					CFiles[i] = CFiles[j];
					CFiles[j] = temp;
				}

		}

	}

	public boolean isCFile(String sName)
	{
		int len = sName.length();
		if (len < 2)
			return false;
		return sName.charAt(len - 1) == 'c' && sName.charAt(len - 2) == '.';
	}

	public String getPrePath()
	{
		String str = null;
		File file = new File(currentpath + "/gui" + "/preDirPath");
		if (file.exists())
		{
			Scanner scan = null;
			try
			{
				scan = new Scanner(file);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			if (scan.hasNext())
				str = scan.next();
		}
		return str;
	}

	public void savePrePath(String preDirPath)
	{
		try
		{
			FileWriter writer = new FileWriter(currentpath + "/gui" + "/preDirPath");
			writer.write(preDirPath);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void openDirectory()
	{
		if (!shell.isDirSet())
		{
			String s_info = "Please set the directory for gcc,lp_solver,simplesim-3.0 first!";
			JOptionPane.showMessageDialog(null, s_info);
			return;
		}
		dirPath = "";
		dirName = "";
		String preDirPath = getPrePath();
		JFileChooser chooser = null;
		if (preDirPath == null || preDirPath.length() == 0)
			chooser = new JFileChooser();
		else
			chooser = new JFileChooser(preDirPath);
		chooser.setFileSelectionMode(1);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == 0)
		{
			dirPath = chooser.getSelectedFile().getPath();
			dirName = chooser.getSelectedFile().getName();
		} else
		{
			return;
		}
		collectCFile(dirPath);
		splitPane.disPane.setCFiles(CFiles);
		shell.setDir(dirName, dirPath);
		shell.callrm();
		int err = shell.callCompile();
		if (err != 0)
		{
			String s_info = "Error during compilation the benchmark\n";
			s_info = s_info + shell.s_compile_err;
			JOptionPane.showMessageDialog(null, s_info);
			return;
		}
		splitPane.setPath(dirPath, dirName);
		splitPane.display();
		loopconstraintItem.setEnabled(true);
		irjumpItem.setEnabled(true);
		otherconstraintsItem.setEnabled(true);
		analyzeItem.setEnabled(true);
		processorItem.setEnabled(true);
		stopItem.setEnabled(true);
		dumpItem.setEnabled(true);
		simulateItem.setEnabled(true);
		messagePane.reset(true);
		messagePane.reset(false);
		nfile = splitPane.disPane.exportblock.loopinfo.nfile;
		files = new file[nfile];
		for (int i = 0; i < nfile; i++)
			files[i] = new file(splitPane.disPane.exportblock.loopinfo.files[i]);

		pane.setConstraintsFileName(dirPath + "/" + dirName);
		pane.setCFiles(CFiles);
		pane.addComponent();
		irjumpEnable();
		savePrePath(dirPath);
		recursiveEnable();
	}

	public void recursiveEnable()
	{
		File file = new File(dirPath + "/" + dirName + ".rec");
		if (file.exists())
			recursiveItem.setEnabled(true);
	}

	public void recursiveAction()
	{
		Vector vec = new Vector();
		File file = new File(dirPath + "/" + dirName + ".rec");
		Scanner scan = null;
		try
		{
			scan = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		String str = "";
		String temp = "";
		while (scan.hasNext()) 
		{
			temp = scan.next();
			if (temp.compareTo(str) != 0)
			{
				str = temp;
				vec.add(str);
			}
		}
		recDialog dialog = new recDialog(vec);
		dialog.setTitle("Set recursion depth for recurisve functions!");
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void saveRecursive(String proname[], String depth[])
	{
		int size = splitPane.disPane.exportblock.input.procs.length;
		for (int i = 0; i < size; i++)
		{
			String str = splitPane.disPane.exportblock.input.procs[i].name;
		}

	}

	public void openfileAction(ActionEvent evt)
	{
		JFileChooser chooser = new JFileChooser();
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("c");
		filter.setDescription("source file");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == 0)
			System.out.println("You chose to open this file: " + chooser.getSelectedFile().getPath());
		else
			return;
		openfileName = chooser.getSelectedFile().getPath();
		shell.setSource(openfileName);
		shell.callrm();
		loopconstraintItem.setEnabled(true);
		otherconstraintsItem.setEnabled(true);
		analyzeItem.setEnabled(true);
		processorItem.setEnabled(true);
		stopItem.setEnabled(true);
		dumpItem.setEnabled(true);
		simulateItem.setEnabled(true);
		messagePane.reset(true);
		messagePane.reset(false);
		nloop = splitPane.disPane.exportblock.loopinfo.nloop;
		loops = splitPane.disPane.exportblock.loopinfo.loops;
		dependentloop = 0;
		for (int index = 0; index < nloop; index++)
			if (!loops[index].bset)
				dependentloop++;

	}

	public void setSolveDir()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Set ILP-solver Location");
		chooser.setFileSelectionMode(1);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == 0)
		{
			System.out.println("You chose to open this directory: " + chooser.getSelectedFile().getPath());
			shell.setlpSolverDir(chooser.getSelectedFile().getPath());
		}
	}

	public void setSimplesimDir()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Set Simplesim-3.0 Location");
		chooser.setFileSelectionMode(1);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == 0)
		{
			System.out.println("You chose to open this directory: " + chooser.getSelectedFile().getPath());
			shell.setSimplesimDir(chooser.getSelectedFile().getPath());
		}
	}

	public void configAction(ActionEvent evt)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Set Gcc Location");
		chooser.setFileSelectionMode(1);
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == 0)
		{
			System.out.println("You chose to open this directory: " + chooser.getSelectedFile().getPath());
			shell.setGcc(chooser.getSelectedFile().getPath());
		}
	}

	public void otherconstraintsAction(ActionEvent evt)
	{
		String str = "";
		str = str + "\n";
		str = str + "For Example: line1 - 20line3 > 10";
		JLabel lab = new JLabel(str);
		Object object[] = {
			lab, pane
		};
		JOptionPane panel = new JOptionPane(((Object) (object)), 3, 2);
		JDialog dialog = panel.createDialog(this, "Set Constraints");
		pane.initListModel();
		dialog.setDefaultCloseOperation(0);
		dialog.setVisible(true);
		dialog.setTitle("Other Constraints");
		int value = ((Integer)panel.getValue()).intValue();
		if (value == 0)
		{
			pane.setExportblock(splitPane.disPane.getExportblock());
			pane.SaveConstraint();
		}
	}

	public void loopConstraintAction()
	{
		for (int k = 0; k < nfile; k++)
		{
			int len = files[k].nloop;
			for (int i = 0; i < len; i++)
			{
				for (int j = i + 1; j < len; j++)
				{
					if (files[k].loops[i].beginline > files[k].loops[j].beginline)
					{
						loop l = files[k].loops[i];
						files[k].loops[i] = files[k].loops[j];
						files[k].loops[j] = l;
					}
					if (files[k].loops[i].beginline == files[k].loops[j].beginline && files[k].loops[i].endline > files[k].loops[j].endline)
					{
						loop l = files[k].loops[i];
						files[k].loops[i] = files[k].loops[j];
						files[k].loops[j] = l;
					}
				}

			}

		}

		dependentloop = 0;
		for (int i = 0; i < nfile; i++)
		{
			for (int j = 0; j < files[i].nloop; j++)
				if (!files[i].loops[j].bset)
					dependentloop++;

		}

		int index = 0;
		if (dependentloop > 0)
		{
			LoopDialog dialog = new LoopDialog(dependentloop);
			setindex = new int[dependentloop];
			for (int i = 0; i < nfile; i++)
			{
				for (int j = 0; j < files[i].nloop; j++)
					if (!files[i].loops[j].bset)
					{
						String name = files[i].loops[j].name + " loop from " + files[i].loops[j].beginline + " to " + files[i].loops[j].endline + " in " + files[i].filename;
						dialog.setLoopName(index, name);
						setindex[index] = files[i].loops[j].iblock;
						index++;
					}

			}

			dialog.pack();
			dialog.setTitle("Input loop bounds!");
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		} else
		{
			JOptionPane.showMessageDialog(null, "All loop bounds have been set");
		}
	}

	public void reload()
	{
		shell.callrm();
		int err = shell.callCompile();
		if (err != 0)
		{
			String s_info = "Error during compilation the benchmark\n";
			s_info = s_info + shell.s_compile_err;
			JOptionPane.showMessageDialog(null, s_info);
			return;
		}
		splitPane.display();
		nfile = splitPane.disPane.exportblock.loopinfo.nfile;
		files = new file[nfile];
		for (int i = 0; i < nfile; i++)
			files[i] = new file(splitPane.disPane.exportblock.loopinfo.files[i]);

	}

	public void irjumpEnable()
	{
		File file = new File(dirPath + "/" + dirName + ".ir");
		njump = 0;
		if (file.exists())
		{
			bir_jump = true;
			Scanner scan = null;
			try
			{
				scan = new Scanner(file);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			for (; scan.hasNextLine(); scan.nextLine())
				njump++;

			irjumpItem.setEnabled(true);
			file = new File(dirPath + "/" + dirName + ".jtable");
			if (file.exists())
				bir_set = true;
			else
				bir_set = false;
		} else
		{
			bir_jump = false;
			irjumpItem.setEnabled(false);
			bir_set = false;
		}
	}

	public void irjumpAction()
	{
		JrDialog dialog = new JrDialog(njump);
		dialog.setTitle("Set targets for Indirect jumps!");
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void aboutAction(ActionEvent evt)
	{
		JOptionPane.showMessageDialog(null, s_info);
	}

	public void savefileAction(ActionEvent evt)
	{
		pane.close();
	}

	public void writetofile()
	{
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(splitPane.disPane.exportblock.consfilename);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		String str = "";
		str = str + splitPane.disPane.exportblock.cons_dataflow;
		str = str + splitPane.disPane.exportblock.cons_loop;
		str = str + splitPane.disPane.exportblock.cons_line;
		try
		{
			writer.write(str);
			writer.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	public void analyzeAction(ActionEvent evt)
	{
		if (bir_jump && !bir_set)
		{
			JOptionPane.showMessageDialog(null, "Set targets for indirect jump first!");
			return;
		} else
		{
			writetofile();
			messagePane.showWaiting();
			final Component frame = this;
			(new Thread() {

				public void run()
				{
					String err = "";
					err = shell.callAnalyze();
					if (shell.bfail)
					{
						if (err.equals(""))
							err = "error during estimation!";
						JOptionPane.showMessageDialog(frame, err, "Error!", 0);
						messagePane.reset(true);
						return;
					} else
					{
						System.out.println(err);
						Scanner scan = new Scanner(err);
						String str[] = new String[36];
						str[0] = "WCET:";
						str[1] = scan.next();
						str[6] = "Branch Misprediction:";
						str[7] = scan.next();
						str[12] = "Instruction Cache Miss:";
						str[13] = scan.next();
						messagePane.showWectResult(str);
						return;
					}
				}

			}).start();
			return;
		}
	}

	public void dumpAction(ActionEvent actionevent)
	{
	}

	public void processorAction(ActionEvent evt)
	{
		processorDialog dialog = new processorDialog();
		dialog.setTitle("Processor configuration!");
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void simulateAction()
	{
		String result = "";
		result = shell.callSimulate();
		if (result.equals("error"))
		{
			JOptionPane.showMessageDialog(null, "Invalid Simulation!");
			return;
		}
		Scanner scan = new Scanner(result);
		String str[] = new String[36];
		str[0] = "Simulation Cycles:";
		str[1] = scan.next();
		if (str[1].equals("Error"))
		{
			JOptionPane.showMessageDialog(null, result);
			return;
		} else
		{
			str[6] = "Branch Misprediction:";
			str[7] = scan.next();
			str[12] = "Instruction Cache Miss:";
			str[13] = scan.next();
			System.out.println("simulate:" + result);
			messagePane.showSimulate(str);
			return;
		}
	}

	public void setcurrentpath()
	{
//		int len = currentpath.length() - 3;
//		currentpath = currentpath.substring(0, len);
	}

	public static void prepath()
	{
		int len = currentpath.length();
		int i;
		for (i = len - 1; i >= 0; i--)
			if (currentpath.charAt(i) == '/')
				break;

		currentpath = currentpath.substring(0, i);
	}

	public static void main(String args[])
	{
		currentpath = (new File(System.getProperty("java.class.path"))).getAbsolutePath();
		System.out.println(currentpath);
		prepath();
		MainFrame f = new MainFrame();
		f.pack();
		f.setVisible(true);
	}






}
