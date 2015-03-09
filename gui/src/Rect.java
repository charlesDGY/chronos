// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Rect.java

package src;

import java.awt.Component;
import java.awt.Graphics2D;

public class Rect extends Component
{

	public int upperleft;
	public int upperright;
	public int width;
	public int height;

	public Rect(int upperleft, int upperright, int width, int height)
	{
		this.upperleft = upperleft;
		this.upperright = upperright;
		this.width = width;
		this.height = height;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getUpperleft()
	{
		return upperleft;
	}

	public void setUpperleft(int upperleft)
	{
		this.upperleft = upperleft;
	}

	public int getUpperright()
	{
		return upperright;
	}

	public void setUpperright(int upperright)
	{
		this.upperright = upperright;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void drawRect(Graphics2D graphics2d)
	{
	}
}
