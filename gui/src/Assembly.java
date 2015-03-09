// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Assembly.java

package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Assembly
{

	public static String over = "end_addr";
	public static String start = "start_addr";
	public String filename;
	public Scanner scanner;
	public String line;
	public String lastadr;
	public File file;
	public int lines;
	boolean bstart;
	boolean bopen;

	public int getLines()
	{
		return lines;
	}

	public void setLines(int lines)
	{
		this.lines = lines;
	}

	public Assembly(String dir)
	{
		bstart = false;
		bopen = true;
		lastadr = "";
		String str = dir + ".dis";
		file = new File(str);
	}

	public boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}

	public boolean isLetter(char c)
	{
		return c >= 'a' && c <= 'z';
	}

	public boolean isLineValid(String word)
	{
		int len = word.length();
		if (len != 8)
			return false;
		for (int i = 0; i < 8; i++)
		{
			char c = word.charAt(i);
			if (!isDigit(c) && !isLetter(c))
				return false;
		}

		return true;
	}

	public void firstOpen()
	{
		while (bopen) 
		{
			bopen = false;
			try
			{
				scanner = new Scanner(file);
				if (scanner.findInLine(start) == null)
					bopen = true;
			}
			catch (FileNotFoundException e)
			{
				bopen = true;
			}
		}
		while (scanner.hasNextLine()) 
		{
			line = scanner.nextLine();
			if (!line.equals(""))
				break;
		}
	}

	public String getAssembly(String from, String to)
	{
		String assembly = "";
		String word = "";
		if (bopen)
		{
			line = "";
			lines = 0;
			firstOpen();
		}
		bstart = false;
		while (!line.equals(over)) 
		{
			Scanner wordscanner = new Scanner(line);
			word = wordscanner.next();
			if (bstart)
			{
				if (word.compareTo(to) == 0)
					break;
				if (isLineValid(word))
				{
					assembly = assembly + word + " ";
					wordscanner.next();
					while (wordscanner.hasNext()) 
						assembly = assembly + wordscanner.next() + " ";
					assembly = assembly + "\n";
					lines++;
				}
			} else
			if (word.equals(from))
			{
				bstart = true;
				assembly = assembly + word + " ";
				wordscanner.next();
				while (wordscanner.hasNext()) 
					assembly = assembly + wordscanner.next() + " ";
				assembly = assembly + "\n";
				lines++;
			}
			while (scanner.hasNextLine()) 
			{
				line = scanner.nextLine();
				if (!line.equals(""))
					break;
			}
		}
		return assembly;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename + ":";
	}

}
