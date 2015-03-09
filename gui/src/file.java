// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   file.java

package src;


// Referenced classes of package src:
//			loop

public class file
{

	String filename;
	loop loops[];
	int nloop;

	public file()
	{
	}

	public file(file f)
	{
		filename = f.filename;
		nloop = f.nloop;
		loops = new loop[nloop];
		for (int i = 0; i < nloop; i++)
			loops[i] = new loop(f.loops[i]);

	}
}
