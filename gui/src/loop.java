// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   loop.java

package src;


public class loop
{

	int beginline;
	int endline;
	int length;
	String name;
	int iblock;
	boolean bset;

	public loop()
	{
		bset = false;
		beginline = -1;
		endline = -1;
		name = "";
	}

	public loop(loop l)
	{
		bset = false;
		beginline = l.beginline;
		endline = l.endline;
		name = l.name;
		iblock = l.iblock;
		bset = l.bset;
	}
}
