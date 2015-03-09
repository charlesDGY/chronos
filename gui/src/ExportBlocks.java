// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ExportBlocks.java

package src;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

// Referenced classes of package src:
//			CfgDataStruct, LoopInfo, ConstraintRules, loop, 
//			InputDependent, SimProfile

public class ExportBlocks
{
	public class unrollStruct
	{

		int proc;
		int bn;
		int index;
		int layer;

		public unrollStruct()
		{
		}
	}


	public Vector cfgvector;
	public Vector opcfgvector;
	public ConstraintRules constraint;
	public String directory;
	public String disfilename;
	public String simfilename;
	public String opdisfilename;
	public String opinfofilename;
	public String cfgfilename;
	public String consfilename;
	public String cfilename;
	public String consstring;
	public String unrollfilename;
	public String loopinfofilename;
	public String stroutput;
	public String target;
	public int numberproc;
	public int opnumberproc;
	public Vector opproc;
	public Vector uproc;
	public FileWriter filewriter;
	public CfgDataStruct cfgarray[];
	public CfgDataStruct opcfgarray[];
	public LoopInfo loopinfo;
	public String lastadr;
	public InputDependent input;
	public String cons_dataflow;
	public String cons_loop;
	public String cons_line;
	public String linefilename;
	public String cFiles[];

	public String[] getCFiles()
	{
		return cFiles;
	}

	public void setCFiles(String files[])
	{
		cFiles = files;
	}

	public String getCfilename()
	{
		return cfilename;
	}

	public void setCfilename(String cfilename)
	{
		this.cfilename = cfilename;
	}

	public ExportBlocks()
	{
		disfilename = "";
		simfilename = "";
		opdisfilename = "";
		opinfofilename = "";
		cfgfilename = "";
		consfilename = "";
		cfilename = "";
		consstring = "";
		unrollfilename = "";
		loopinfofilename = "";
		stroutput = "";
		numberproc = 0;
		opnumberproc = 0;
		cons_dataflow = "";
		cons_loop = "";
		cons_line = "";
		cfgvector = new Vector();
		opcfgvector = new Vector();
	}

	public ConstraintRules getConstraint()
	{
		return constraint;
	}

	public void setConstraint(ConstraintRules constraint)
	{
		this.constraint = constraint;
	}

	public void loadUnrollInfo()
	{
		File infofile = new File(unrollfilename);
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(infofile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		Vector v = new Vector();
		int len = cfgarray.length;
		for (int i = 0; i < len; i++)
			if (cfgarray[i].bloop)
			{
				unrollStruct u = new unrollStruct();
				u.proc = cfgarray[i].procnum;
				u.bn = cfgarray[i].blocknum;
				u.index = i;
				u.layer = cfgarray[i].layer;
				v.add(u);
				System.out.println("loop:" + i);
			}

		String procedure = "Procedure";
		int unroll = 0;
		unrollStruct array[] = new unrollStruct[v.size()];
		array = (unrollStruct[])v.toArray(array);
		boolean b = false;
		int p = -1;
		while (scanner.hasNext()) 
		{
			String str = scanner.next();
			if (str.equals(procedure))
			{
				p++;
			} else
			{
				b = false;
				for (; !str.equals(";"); str = scanner.next())
				{
					b = true;
					unroll = Integer.parseInt(str);
				}

				if (!b)
					unroll = 1;
				int i = getIndexOfArrayBlock(array, p);
				cfgarray[i].unroll = unroll;
			}
		}
		System.out.println("unroll");
		print(cfgarray);
	}

	public void print(CfgDataStruct array[])
	{
		int len = array.length;
		for (int i = 0; i < len; i++)
			if (array[i].bloop)
				System.out.println(i + " P : " + array[i].procnum + " B : " + array[i].blocknum + " U : " + array[i].unroll + " L : " + array[i].layer);

	}

	public int getIndexOfArrayBlock(unrollStruct array[], int pn)
	{
		int index = 0;
		int len = array.length;
		boolean b = false;
		for (; index < len; index++)
			if (array[index].layer != 0)
				break;

		int p = array[index].proc;
		int i = index;
		int j = index;
		for (index++; index < len; index++)
		{
			if (array[index].proc != p)
				break;
			j++;
		}

		for (int k = 0; k < len; k++)
			System.out.println(k + " " + array[k].layer);

		for (index = j; index >= i; index--)
		{
			if (index == i)
			{
				array[index].layer = 0;
				return array[index].index;
			}
			if (array[index].layer == array[index - 1].layer && array[index].layer != 0)
			{
				array[index].layer = 0;
				return array[index].index;
			}
			if (array[index].layer > array[index - 1].layer)
			{
				array[index].layer = 0;
				return array[index].index;
			}
		}

		return array[index].index;
	}

	public void setLayer(CfgDataStruct cfgarray[])
	{
		int len = cfgarray.length;
		for (int index = 0; index < len; index++)
			if (cfgarray[index].bloop)
			{
				int i = cfgarray[index].returnblocka;
				if (i != -1 && i <= cfgarray[index].blocknum)
				{
					for (int j = index - (cfgarray[index].blocknum - i); j <= index; j++)
						cfgarray[j].layer++;

				}
				i = cfgarray[index].returnblockb;
				if (i != -1 && i <= cfgarray[index].blocknum)
				{
					for (int j = index - (cfgarray[index].blocknum - i); j <= index; j++)
						cfgarray[j].layer++;

				}
			}

	}

	public boolean loadInfoFile()
	{
		cfgvector.clear();
		numberproc = 0;
		File infofile = new File(cfgfilename);
		Scanner infoscanner = null;
		Scanner linescanner = null;
		String line = "";
		boolean bopen = true;
		while (bopen) 
		{
			bopen = false;
			try
			{
				linescanner = new Scanner(infofile);
			}
			catch (FileNotFoundException e)
			{
				bopen = true;
			}
		}
		String str = "";
		int proc = 0;
		int block = 0;
		int index = 0;
		int argr = 0;
		int num = 0;
		uproc = new Vector();
		Pattern p = Pattern.compile("proc\\S*");
		do
			try
			{
				if (!linescanner.hasNextLine())
					break;
				line = linescanner.nextLine();
				if (!line.equals(""))
				{
					infoscanner = new Scanner(line);
					if (infoscanner.hasNext(p))
					{
						numberproc++;
						if (numberproc != 1)
						{
							num += block + 1;
							uproc.add(Integer.valueOf(block + 1));
						}
						str = infoscanner.next(p);
						index = 5;
						proc = 0;
						for (; str.charAt(index) != ']'; index++)
							proc = (proc * 10 + str.charAt(index)) - 48;

						str = infoscanner.next();
					} else
					{
						CfgDataStruct cfg = new CfgDataStruct();
						block = infoscanner.nextInt();
						infoscanner.next();
						cfg.setProcnum(proc);
						cfg.setBlocknum(block);
						str = infoscanner.next();
						cfg.setAddress(str);
						cfg.setNum(num);
						argr = 0x7fffffff;
						str = infoscanner.next();
						str = infoscanner.next();
						str = infoscanner.next();
						boolean boola = true;
						for (; !str.equals("]"); str = infoscanner.next())
							if (!str.equals(","))
							{
								if (boola)
								{
									cfg.setReturnblocka(Integer.parseInt(str));
									boola = false;
								} else
								{
									cfg.setReturnblockb(Integer.parseInt(str));
								}
								if (argr > Integer.parseInt(str))
									argr = Integer.parseInt(str);
							}

						if (argr <= block)
							cfg.setBloop(true);
						cfgvector.add(cfg);
					}
				}
			}
			catch (Exception e)
			{
				return false;
			}
		while (true);
		uproc.add(Integer.valueOf(block + 1));
		infoscanner.close();
		linescanner.close();
		cfgarray = new CfgDataStruct[cfgvector.size()];
		cfgarray = (CfgDataStruct[])cfgvector.toArray(cfgarray);
		LoadOp();
		return true;
	}

	public void LoadOp()
	{
		setLayer(cfgarray);
		loopinfo = null;
		loopinfo = new LoopInfo();
		loopinfo.setCFiles(cFiles);
		loopinfo.setCfilename(cfilename);
		loopinfo.setDirectory(loopinfofilename);
		loopinfo.setDisfilename(disfilename);
		loopinfo.readLoopInfo();
		loopinfo.MapBlockToLoop(cfgarray, disfilename);
	}

	public void LoadOpInfoFile()
	{
		opcfgvector.clear();
		opnumberproc = 0;
		opproc = new Vector();
		File file = new File(opinfofilename);
		Scanner infoscanner = null;
		try
		{
			infoscanner = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		String str = "";
		int proc = 0;
		int block = 0;
		int index = 0;
		int argr = 0;
		int num = 0;
		Pattern p = Pattern.compile("proc\\S*");
		do
			if (infoscanner.hasNext(p))
			{
				opnumberproc++;
				if (opnumberproc != 1)
				{
					num += block + 1;
					opproc.add(Integer.valueOf(block + 1));
				}
				str = infoscanner.next(p);
				index = 5;
				proc = 0;
				for (; str.charAt(index) != ']'; index++)
					proc = (proc * 10 + str.charAt(index)) - 48;

				str = infoscanner.next();
			} else
			{
				CfgDataStruct cfg = new CfgDataStruct();
				block = infoscanner.nextInt();
				infoscanner.next();
				cfg.setProcnum(proc);
				cfg.setBlocknum(block);
				str = infoscanner.next();
				cfg.setAddress(str);
				cfg.setNum(num);
				argr = 0x7fffffff;
				str = infoscanner.next();
				str = infoscanner.next();
				str = infoscanner.next();
				boolean boola = true;
				for (; !str.equals("]"); str = infoscanner.next())
					if (!str.equals(","))
					{
						if (boola)
						{
							cfg.setReturnblocka(Integer.parseInt(str));
							boola = false;
						} else
						{
							cfg.setReturnblockb(Integer.parseInt(str));
						}
						if (argr > Integer.parseInt(str))
							argr = Integer.parseInt(str);
					}

				if (argr <= block)
					cfg.setBloop(true);
				opcfgvector.add(cfg);
			}
		while (infoscanner.hasNext());
		opproc.add(Integer.valueOf(block + 1));
		infoscanner.close();
		opcfgarray = new CfgDataStruct[opcfgvector.size()];
		opcfgarray = (CfgDataStruct[])opcfgvector.toArray(opcfgarray);
	}

	public int findBlock(String disaddress)
	{
		int len = cfgvector.size();
		for (int index = 0; index < len; index++)
		{
			CfgDataStruct cfg = (CfgDataStruct)cfgvector.get(index);
			if (disaddress.compareTo(cfg.getAddress()) >= 0 && index + 1 < len)
			{
				CfgDataStruct next = (CfgDataStruct)cfgvector.get(index + 1);
				if (next.procnum == cfg.procnum && disaddress.compareTo(next.getAddress()) < 0)
					return index;
				if (next.procnum == cfg.procnum + 1 && disaddress.compareTo(next.getAddress()) < 0)
					return index;
			}
		}

		return -1;
	}

	public boolean doConstraint()
	{
		int num = constraint.getNum();
		int linenums[] = constraint.getLinenums();
		int indexl = 0;
		int indexv = 0;
		int arg = 0;
		boolean bline = true;
		CfgDataStruct cfg = null;
		consstring = "";
		for (indexl = 0; indexl <= num; indexl++)
		{
			if (indexl != 0)
				consstring += " " + constraint.chars[indexl - 1] + " ";
			indexv = getCfgNum(linenums[indexl]);
			arg = constraint.argnums[indexl];
			if (indexv == -1)
			{
				Scanner disscanner = null;
				try
				{
					File disfile = new File(disfilename);
					disscanner = new Scanner(disfile);
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				Vector disaddress = new Vector();
				String disfind = cfilename + ":" + linenums[indexl];
				String address = "";
				while (disscanner.hasNextLine()) 
				{
					address = disscanner.nextLine();
					String s;
					if (address.equals(disfind))
						disaddress.add(s = disscanner.next());
				}
				disscanner.close();
				if (disaddress.size() == 0)
					return false;
				if (disaddress.size() > 1)
				{
					indexv = findBlock((String)disaddress.get(0));
					int j = disaddress.size();
					for (int i = 1; i < j; i++)
						if (findBlock((String)disaddress.get(i)) != indexv)
							return false;

					if (indexv == -1)
						return false;
					cfgarray[indexv].linenumber = linenums[indexl];
					if (arg != 1)
						consstring += arg + " ";
					consstring += "c" + cfgarray[indexv].procnum + "." + cfgarray[indexv].blocknum;
				} else
				{
					indexv = findBlock((String)disaddress.get(0));
					if (indexv == -1)
						return false;
					cfgarray[indexv].linenumber = linenums[indexl];
					if (arg != 1)
						consstring += arg + " ";
					consstring += "c" + cfgarray[indexv].procnum + "." + cfgarray[indexv].blocknum;
				}
			} else
			{
				if (arg != 1)
					consstring += arg + " ";
				consstring += "c" + cfgarray[indexv].procnum + "." + cfgarray[indexv].blocknum;
			}
		}

		consstring += " " + constraint.getSequal() + " " + constraint.getRightnumber();
		System.out.println(consstring);
		cons_line += consstring;
		cons_line += "\n";
		consstring = "";
		return true;
	}

	public boolean isLoop(int index)
	{
		if (cfgarray[index].returnblocka != -1 && cfgarray[index].returnblocka <= cfgarray[index].blocknum)
			return true;
		return cfgarray[index].returnblockb != -1 && cfgarray[index].returnblockb <= cfgarray[index].blocknum;
	}

	public void setInnerBlock()
	{
		int len = cfgarray.length;
		for (int i = 0; i < len; i++)
			if (cfgarray[i].bloop && cfgarray[i].unroll > 1)
			{
				int k = cfgarray[i].returnblocka;
				if (k == -1 || k > cfgarray[i].blocknum)
					k = cfgarray[i].returnblockb;
				k += cfgarray[i].num;
				for (int j = k; j < i; j++)
					cfgarray[j].unroll *= cfgarray[i].unroll;

			}

	}

	public int getCfgNum(int line)
	{
		int size = cfgarray.length;
		int index = 0;
		for (index = 0; index < size; index++)
			if (cfgarray[index].linenumber == line)
				return index;

		return -1;
	}

	public void output(String str)
	{
		System.out.println("out:" + str);
		try
		{
			filewriter.write(str);
			filewriter.write(10);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			filewriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public void setFileNames(String target, String dirPath)
	{
		if (directory == null)
		{
			return;
		} else
		{
			this.target = target;
			disfilename = dirPath + "/" + target + ".dis";
			simfilename = dirPath + "/" + target + ".out";
			cfgfilename = dirPath + "/" + target + ".cfg";
			consfilename = dirPath + "/" + target + ".cons";
			loopinfofilename = dirPath + "/" + "loop.info";
			linefilename = dirPath + "/" + "line.info";
			return;
		}
	}

	public void open()
	{
		try
		{
			filewriter = new FileWriter(consfilename, true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setLoopBoundConstraints(int block[], int count[], String operator[])
	{
		int len = count.length;
		int index = 0;
		String str = "";
		cons_loop = "";
		for (; index < len; index++)
		{
			int blocknum = block[index];
			int header = findloopheader(blocknum);
			int arg = count[index];
			String opr = operator[index];
			if (arg != -1)
				if (header < 0)
				{
					System.out.println("Invalid loop header");
				} else
				{
					str = "c" + cfgarray[blocknum].procnum + "." + cfgarray[blocknum].blocknum;
					str = str + "- " + arg + " c" + cfgarray[header].procnum + "." + cfgarray[header].blocknum;
					str = str + " " + opr;
					str = str + "  0";
					cons_loop += str;
					cons_loop += "\n";
					System.out.println(str);
				}
		}

	}

	public void setLoopBound(int array[], int count[], String less[])
	{
		int len = count.length;
		int index = 0;
		String str = "";
		cons_loop = "";
		for (; index < len; index++)
		{
			int arg = count[index];
			System.out.println("arg " + arg);
			System.out.println("id " + array[index]);
			int id = loopinfo.loops[array[index]].iblock;
			String sign = less[index];
			if (!loopinfo.loops[array[index]].bset)
			{
				int header = findloopheader(id);
				str = "";
				if (header < 0)
				{
					System.out.println("Invalid Loop header!");
				} else
				{
					str = "c" + cfgarray[id].procnum + "." + cfgarray[id].blocknum;
					str = str + "- " + arg + " c" + cfgarray[header].procnum + "." + cfgarray[header].blocknum;
					str = str + " " + sign;
					str = str + "  0";
					cons_loop += str;
					cons_loop += "\n";
				}
			}
		}

	}

	public int findloopheader(int id)
	{
		int header = 0;
		if (cfgarray[id].returnblocka <= cfgarray[id].blocknum && cfgarray[id].returnblocka != -1)
			header = cfgarray[id].returnblocka;
		if (cfgarray[id].returnblockb <= cfgarray[id].blocknum && cfgarray[id].returnblockb != -1)
			header = cfgarray[id].returnblockb;
		header = (header + cfgarray[id].num) - 1;
		return header;
	}

	public void DataFlow(String filename)
	{
		boolean b = true;
		input = new InputDependent(directory + "/" + target);
		input.setFilename(filename);
		input.setCFiles(cFiles);
		input.ReadCodes();
		input.ReadBlocks(uproc, cfgarray);
		System.out.println("data flow analysis start");
		try
		{
			input.DataFlowAnalysis();
		}
		catch (Exception e)
		{
			b = false;
			System.out.println("data flow analysis finish 0");
			e.printStackTrace();
			return;
		}
		if (b)
		{
			System.out.println("data flow analysis finish 1");
			setProfileConstraint();
		}
	}

	public void setProfileConstraint()
	{
		String str = "";
		SimProfile sim = new SimProfile(simfilename);
		sim.setLastadr(lastadr);
		sim.getCount();
		cons_dataflow = "";
		cons_loop = "";
		cons_line = "";
		int i1 = input.procs.length;
		int k = 0;
		for (int i = 0; i < i1; i++)
		{
			int j1 = input.procs[i].blocks.length;
			for (int j = 0; j < j1; j++)
				if (!input.procs[i].blocks[j].idepend)
				{
					int num = sim.GetNum(cfgarray[k + j].address);
					if (num != 0)
					{
						str = "c" + i + "." + j + " = " + num;
						cons_dataflow += str;
						cons_dataflow += "\n";
						if (cfgarray[k + j].bloop)
							loopinfo.setLoopDependent(k + j, true);
					} else
					{
						str = "c" + i + "." + j + " = " + 0;
						cons_dataflow += str;
						cons_dataflow += "\n";
						if (cfgarray[k + j].bloop)
							loopinfo.setLoopDependent(k + j, true);
					}
				} else
				if (cfgarray[k + j].bloop)
					loopinfo.setLoopDependent(k + j, false);

			k += j1;
		}

	}
}
