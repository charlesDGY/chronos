// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SimProfile.java

package src;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class SimProfile
{
	public class AdrNum
	{

		public String adr;
		public int num;

		public AdrNum()
		{
		}
	}


	public static String leftbracelet = "(";
	public String simstr;
	public File simfile;
	public Vector vec;
	public Scanner scanner;
	public String lastadr;

	public SimProfile(String file)
	{
		lastadr = "";
		simfile = new File(file);
	}

	public void getCount()
	{
		try
		{
			scanner = new Scanner(simfile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		vec = new Vector();
		setAdrNum();
		scanner.close();
	}

	public void setAdrNum()
	{
		boolean bstart = false;
		Scanner wordscanner;
		for (; scanner.hasNextLine(); wordscanner.close())
		{
			String line = scanner.nextLine();
			wordscanner = new Scanner(line);
			if (!wordscanner.hasNext())
				continue;
			String word = wordscanner.next();
			if (isLineValid(word))
			{
				bstart = true;
				AdrNum adrnum = new AdrNum();
				adrnum.adr = word;
				word = wordscanner.next();
				adrnum.num = Integer.parseInt(word);
				vec.add(adrnum);
				continue;
			}
			if (bstart)
				break;
		}

	}

	public boolean isEnd(String adr)
	{
		return adr.substring(2).endsWith(lastadr.substring(2));
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

	public int GetNum(String adr)
	{
		int index = 0;
		for (int len = vec.size(); index < len; index++)
		{
			AdrNum adrnum = (AdrNum)vec.get(index);
			if (adrnum.adr.substring(2).equals(adr.substring(2)))
				return adrnum.num;
		}

		return 0;
	}

	public void print()
	{
		int index = 0;
		for (int len = vec.size(); index < len; index++)
		{
			System.out.print(((AdrNum)vec.get(index)).adr + " ");
			System.out.println(((AdrNum)vec.get(index)).num);
		}

	}

	public String getLastadr()
	{
		return lastadr;
	}

	public void setLastadr(String lastadr)
	{
		this.lastadr = lastadr;
	}

}
