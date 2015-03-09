// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ConstraintRules.java

package src;


public class ConstraintRules
{

	public String mstring;
	public static String s_line = "line";
	public static int MAXOPERATOR = 100;
	public String sequal;
	public int index;
	public int len;
	public int rightnumber;
	public char chars[];
	public int argnums[];
	public int linenums[];
	public int num;

	public boolean isPlusorMinus(char c)
	{
		return c == '+' || c == '-';
	}

	public boolean equalValid()
	{
		return sequal.equals(">") || sequal.equals(">=") || sequal.equals("=") || sequal.equals("<") || sequal.equals("<=");
	}

	public ConstraintRules()
	{
		sequal = "";
	}

	public ConstraintRules(String string)
	{
		sequal = "";
		mstring = string;
		index = 0;
		num = 0;
		rightnumber = 0;
		len = mstring.length();
		chars = new char[MAXOPERATOR];
		argnums = new int[MAXOPERATOR];
		linenums = new int[MAXOPERATOR];
	}

	public boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}

	public boolean isLetter(char c)
	{
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	public boolean isEnd()
	{
		return index < len && (mstring.charAt(index) == '>' || mstring.charAt(index) == '=' || mstring.charAt(index) == '<');
	}

	public boolean doString()
	{
		for (; !isEnd(); doSpace())
		{
			doSpace();
			if (!getNum(true))
				return false;
			doSpace();
			if (!getLine())
				return false;
			doSpace();
			if (!getNum(false))
				return false;
			doSpace();
			if (index >= len || !isPlusorMinus(mstring.charAt(index)))
				break;
			chars[num] = mstring.charAt(index);
			index++;
			num++;
		}

		while (isEnd()) 
		{
			sequal += mstring.charAt(index);
			index++;
		}
		if (!equalValid())
			return false;
		doSpace();
		for (; index < len && isDigit(mstring.charAt(index)); index++)
			rightnumber = (rightnumber * 10 + mstring.charAt(index)) - 48;

		doSpace();
		return index >= len;
	}

	public boolean getNum(boolean arg)
	{
		int number = 0;
		if (index >= len || mstring.charAt(index) == '-')
			return false;
		for (; index < len && isDigit(mstring.charAt(index)); index++)
			number = (number * 10 + mstring.charAt(index)) - 48;

		if (arg)
		{
			if (number == 0)
				argnums[num] = 1;
			else
				argnums[num] = number;
		} else
		{
			if (number == 0)
				return false;
			linenums[num] = number;
		}
		return true;
	}

	public boolean getLine()
	{
		String s = new String();
		for (; index < len && isLetter(mstring.charAt(index)); index++)
			s = s + mstring.charAt(index);

		return s.equals(s_line);
	}

	public void doSpace()
	{
		for (; index < len && mstring.charAt(index) == ' '; index++);
	}

	public String getString()
	{
		int i = 0;
		String str = new String();
		for (i = 0; i <= num; i++)
		{
			if (argnums[i] != 1)
				str = str + argnums[i];
			str = str + s_line;
			str = str + linenums[i];
			if (i != num)
				str = str + chars[i];
		}

		str = str + sequal;
		str = str + rightnumber;
		return str;
	}

	public int[] getArgnums()
	{
		return argnums;
	}

	public void setArgnums(int argnums[])
	{
		this.argnums = argnums;
	}

	public char[] getChars()
	{
		return chars;
	}

	public void setChars(char chars[])
	{
		this.chars = chars;
	}

	public int[] getLinenums()
	{
		return linenums;
	}

	public void setLinenums(int linenums[])
	{
		this.linenums = linenums;
	}

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getRightnumber()
	{
		return rightnumber;
	}

	public void setRightnumber(int rightnumber)
	{
		this.rightnumber = rightnumber;
	}

	public String getSequal()
	{
		return sequal;
	}

	public void setSequal(String sequal)
	{
		this.sequal = sequal;
	}

	public String getMstring()
	{
		return mstring;
	}

	public void setMstring(String mstring)
	{
		this.mstring = mstring;
		index = 0;
		num = 0;
		rightnumber = 0;
		len = mstring.length();
		sequal = "";
		chars = new char[MAXOPERATOR];
		argnums = new int[MAXOPERATOR];
		linenums = new int[MAXOPERATOR];
	}

}
