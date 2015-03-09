// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LoopInfo.java

package src;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

// Referenced classes of package src:
//			loop, file, CfgDataStruct

public class LoopInfo
{
	public class pair
	{

		String adr;
		int ifile;
		int ln;

		public pair()
		{
		}
	}


	public String directory;
	public String cfilename;
	public String disfilename;
	public loop loops[];
	public int nloop;
	public file files[];
	public int nfile;
	public String cFiles[];
	public static int MaxLoopNum = 100;

	public LoopInfo()
	{
		loops = null;
		files = null;
	}

	public String getDisfilename()
	{
		return disfilename;
	}

	public void setDisfilename(String disfilename)
	{
		this.disfilename = disfilename;
	}

	public String getCfilename()
	{
		return cfilename;
	}

	public void setCfilename(String cfilename)
	{
		this.cfilename = cfilename;
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public int getLoopIndex(int iblock)
	{
		for (int i = 0; i < nloop; i++)
			if (loops[i].iblock == iblock)
				return i;

		return -1;
	}

	public void setLoopDependent(int iblock, boolean bdependent)
	{
		for (int i = 0; i < nfile; i++)
		{
			int k = files[i].nloop;
			for (int j = 0; j < k; j++)
				if (files[i].loops[j].iblock == iblock)
				{
					files[i].loops[j].bset = bdependent;
					return;
				}

		}

		System.out.println("not find");
	}

	public void readLoopInfo()
	{
		File file = new File(directory);
		Scanner scanner = null;
		String str = null;
		try
		{
			scanner = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		files = new file[cFiles.length];
		nfile = cFiles.length;
		int stack[] = new int[MaxLoopNum];
		int top = -1;
		int index = -1;
		int findex = -1;
		while (scanner.hasNext()) 
		{
			str = scanner.next();
			if (str.equals("loop"))
			{
				if (findex != -1)
				{
					files[findex].loops = loops;
					files[findex].nloop = index + 1;
					SortByLength(findex);
				}
				findex++;
				files[findex] = new file();
				files[findex].filename = cFiles[findex];
				files[findex].nloop = 0;
				index = -1;
				top = -1;
				loops = new loop[MaxLoopNum];
			} else
			if (str.equals("for") || str.equals("while") || str.equals("do"))
			{
				index++;
				top++;
				stack[top] = index;
				loops[index] = new loop();
				loops[index].name = str;
			} else
			if (loops[stack[top]].beginline == -1)
				loops[stack[top]].beginline = Integer.parseInt(str);
			else
			if (loops[stack[top]].endline == -1)
			{
				loops[stack[top]].endline = Integer.parseInt(str);
				loops[stack[top]].length = loops[stack[top]].endline - loops[stack[top]].beginline;
				top--;
			}
		}
		files[findex].loops = loops;
		files[findex].nloop = index + 1;
		SortByLength(findex);
		scanner.close();
	}

	public void printfile()
	{
		System.out.println("loop mapping:");
		for (int i = 0; i < nfile; i++)
		{
			System.out.println(files[i].filename);
			for (int j = 0; j < files[i].nloop; j++)
				System.out.println(files[i].loops[j].name + " " + files[i].loops[j].beginline + " " + files[i].loops[j].endline + " maps to block" + files[i].loops[j].iblock);

		}

	}

	public void SortByLength(int index)
	{
		int len = files[index].nloop;
		for (int i = 0; i < len; i++)
		{
			for (int j = i + 1; j < len; j++)
				if (files[index].loops[i].length > files[index].loops[j].length)
				{
					loop l = files[index].loops[i];
					files[index].loops[i] = files[index].loops[j];
					files[index].loops[j] = l;
				}

		}

	}

	public int isSourceLine(String str)
	{
		for (int i = 0; i < nfile; i++)
		{
			int len = cFiles[i].length();
			int k = str.length();
			if (k >= len)
			{
				int j;
				for (j = 0; j < len; j++)
					if (str.charAt(j) != cFiles[i].charAt(j))
						break;

				if (j >= len)
					return i;
			}
		}

		return -1;
	}

	public int getLine(String str)
	{
		int len = str.length();
		int line = 0;
		int index;
		for (index = 0; str.charAt(index) != ':'; index++);
		for (index++; index < len; index++)
			line = (line * 10 + str.charAt(index)) - 48;

		return line;
	}

	public void MapBlockToLoop(CfgDataStruct cfgarray[], String filename)
	{
		Vector v = new Vector();
		File file = new File(filename);
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(file);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		String str = null;
		String adr = null;
		while (scanner.hasNextLine()) 
		{
			str = scanner.nextLine();
			int pos = isSourceLine(str);
			if (pos != -1)
			{
				adr = scanner.next();
				pair p = new pair();
				p.adr = adr;
				p.ln = getLine(str);
				p.ifile = pos;
				v.add(p);
			}
		}
		int ifile = 0;
		int lineto = 0x80000000;
		int linefrom = 0x7fffffff;
		String adrto = null;
		String adrfrom = null;
		int leni = cfgarray.length;
		for (int i = 0; i < leni; i++)
			if (cfgarray[i].bloop)
			{
				lineto = 0x80000000;
				linefrom = 0x7fffffff;
				adrto = cfgarray[i + 1].address;
				adrto = GetLastAddress(adrto);
				int k = cfgarray[i].returnblocka;
				if (k == -1 || k > cfgarray[i].blocknum)
					k = cfgarray[i].returnblockb;
				adrfrom = cfgarray[k + cfgarray[i].num].address;
				int len = v.size();
				pair p = null;
				boolean b = false;
				int index;
				for (index = 0; index < len; index++)
				{
					p = (pair)v.get(index);
					if (!b)
					{
						if (adrfrom.compareTo(p.adr) == 0)
						{
							linefrom = min(p.ln, linefrom);
							lineto = linefrom;
							b = true;
							ifile = p.ifile;
						} else
						if (adrfrom.compareTo(p.adr) < 0)
						{
							p = (pair)v.get(index - 1);
							linefrom = min(p.ln, linefrom);
							lineto = linefrom;
							b = true;
							ifile = p.ifile;
						}
						continue;
					}
					if (adrto.compareTo(p.adr) == 0)
					{
						linefrom = min(p.ln, linefrom);
						lineto = max(p.ln, lineto);
						break;
					}
					if (adrto.compareTo(p.adr) < 0)
						break;
					linefrom = min(p.ln, linefrom);
					lineto = max(p.ln, lineto);
				}

				index = GetLoopIndex(linefrom, lineto, ifile);
				if (index == -1)
				{
					cfgarray[i].bloop = false;
				} else
				{
					cfgarray[i].linenumber = files[ifile].loops[index].beginline;
					cfgarray[i].ifile = ifile;
					files[ifile].loops[index].iblock = i;
				}
			}

		printfile();
	}

	public String GetLastAddress(String address)
	{
		int index = address.length() - 1;
		char ch[] = new char[index + 1];
		boolean b = false;
		ch = address.toCharArray();
		if (ch[index] == '8')
		{
			ch[index] = '0';
		} else
		{
			ch[index] = '8';
			b = true;
		}
		index--;
		while (b) 
			if (ch[index] == '0')
			{
				ch[index] = 'f';
				index--;
			} else
			{
				if (ch[index] == 'a')
					ch[index] = '9';
				else
					ch[index]--;
				b = false;
			}
		return new String(ch);
	}

	public int GetLoopIndex(int linefrom, int lineto, int ifile)
	{
		for (int i = 0; i < files[ifile].nloop; i++)
			if (files[ifile].loops[i].beginline <= linefrom && files[ifile].loops[i].endline >= lineto)
				return i;

		return -1;
	}

	public int min(int a, int b)
	{
		return a >= b ? b : a;
	}

	public int max(int a, int b)
	{
		return a <= b ? b : a;
	}

	public void print()
	{
		System.out.println(nloop);
		for (int i = 0; i < nloop; i++)
			System.out.println(loops[i].name + " " + loops[i].beginline + " " + loops[i].endline);

	}

	public String[] getCFiles()
	{
		return cFiles;
	}

	public void setCFiles(String files[])
	{
		cFiles = files;
	}

}
