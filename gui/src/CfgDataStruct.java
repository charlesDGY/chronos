// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CfgDataStruct.java

package src;


public class CfgDataStruct
{

	public int procnum;
	public int blocknum;
	public int ifile;
	public int linenumber;
	public boolean bloop;
	public int layer;
	public int unroll;
	public int num;
	public int returnblocka;
	public int returnblockb;
	public String address;

	public int getNum()
	{
		return num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getLayer()
	{
		return layer;
	}

	public void setLayer(int layer)
	{
		this.layer = layer;
	}

	public int getUnroll()
	{
		return unroll;
	}

	public void setUnroll(int unroll)
	{
		this.unroll = unroll;
	}

	public CfgDataStruct()
	{
		bloop = false;
		linenumber = -1;
		returnblocka = -1;
		returnblockb = -1;
		layer = 1;
		unroll = 1;
	}

	public int getReturnblocka()
	{
		return returnblocka;
	}

	public void setReturnblocka(int returnblocka)
	{
		this.returnblocka = returnblocka;
	}

	public int getReturnblockb()
	{
		return returnblockb;
	}

	public void setReturnblockb(int returnblockb)
	{
		this.returnblockb = returnblockb;
	}

	public boolean isBloop()
	{
		return bloop;
	}

	public void setBloop(boolean bloop)
	{
		this.bloop = bloop;
	}

	public int getLinenumber()
	{
		return linenumber;
	}

	public void setLinenumber(int linenumber)
	{
		this.linenumber = linenumber;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public int getBlocknum()
	{
		return blocknum;
	}

	public void setBlocknum(int blocknum)
	{
		this.blocknum = blocknum;
	}

	public int getProcnum()
	{
		return procnum;
	}

	public void setProcnum(int procnum)
	{
		this.procnum = procnum;
	}
}
