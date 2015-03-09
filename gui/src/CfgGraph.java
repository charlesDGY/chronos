// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CfgGraph.java

package src;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.LineBorder;

// Referenced classes of package src:
//			Rect, CfgDataStruct, Assembly

public class CfgGraph extends JPanel
	implements MouseMotionListener
{
	public class DrawingPane extends JPanel
	{

		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			if (!bpaint)
				return;
			int index = 0;
			int len = vector.size();
			int proc = 0;
			for (; index < len; index++)
			{
				g2.setColor(Color.BLACK);
				drawRect(g2, index);
				CfgDataStruct cfg = (CfgDataStruct)cfgvector.get(index);
				if (proc != cfg.procnum)
					proc++;
				if (cfg.returnblocka - cfg.blocknum == 1 || cfg.returnblockb - cfg.blocknum == 1)
				{
					g2.setColor(Color.RED);
					drawArrowLine(g2, index);
				}
			}

			g2.setColor(Color.RED);
			ArrowArcWidth = 20;
			len = up.size();
			int last = -1;
			for (index = 0; index < len; index++)
			{
				Pair pair = (Pair)up.get(index);
				drawArrowArc(g2, pair.from, pair.to, ArrowArcWidth, false);
				if (pair.from - pair.to != last)
				{
					last = pair.from - pair.to;
					ArrowArcWidth += 10;
				}
			}

			ArrowArcWidth = 20;
			len = down.size();
			last = -1;
			for (index = 0; index < len; index++)
			{
				Pair pair = (Pair)down.get(index);
				drawArrowArc(g2, pair.from, pair.to, ArrowArcWidth, true);
				if (pair.from - pair.to != last)
				{
					last = pair.from - pair.to;
					ArrowArcWidth += 10;
				}
			}

			Dimension dimension = new Dimension(getBounds().width, ActualHeight);
			drawingPane.setPreferredSize(dimension);
			drawingPane.revalidate();
		}

		public DrawingPane()
		{
		}
	}

	public class Pair
	{

		public int from;
		public int to;

		public Pair(int from, int to)
		{
			this.from = from;
			this.to = to;
		}
	}


	public Vector vector;
	public int ArrowLineLength;
	public int ArrowArcWidth;
	public int Upperleft;
	public int Upperright;
	public static int Rectwidth = 70;
	public static int Rectheight = 20;
	public static int Inteval = 40;
	public int ActualHeight;
	public Vector cfgvector;
	public Vector up;
	public Vector down;
	public int numbercfg;
	public boolean bpaint;
	private JPanel drawingPane;
	private JFrame frame;
	private JTextArea textArea;
	public JPanel leftPanel;
	public String directory;
	public JFrame mainframe;
	public boolean bstop;
	public Assembly assembly;
	public JScrollPane scroller;

	public JFrame getMainframe()
	{
		return mainframe;
	}

	public void setMainframe(JFrame mainframe)
	{
		this.mainframe = mainframe;
	}

	public String getDirectory()
	{
		return directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public JPanel getLeftPanel()
	{
		return leftPanel;
	}

	public void setLeftPanel(JPanel leftPanel)
	{
		this.leftPanel = leftPanel;
	}

	public Vector getCfgvector()
	{
		return cfgvector;
	}

	public void setCfgvector(Vector cfgvector)
	{
		this.cfgvector = cfgvector;
	}

	public CfgGraph()
	{
		super(new BorderLayout());
		ArrowLineLength = 20;
		ArrowArcWidth = 20;
		Upperleft = 100;
		Upperright = 0;
		ActualHeight = 0;
		bpaint = false;
		bpaint = false;
		bstop = false;
		frame = new JFrame();
		frame.setUndecorated(true);
		textArea = new JTextArea();
		textArea.setBackground(Color.YELLOW);
		LineBorder border = new LineBorder(Color.GREEN);
		textArea.setBorder(border);
		frame.getContentPane().add(textArea);
		frame.setVisible(false);
		drawingPane = new DrawingPane();
		drawingPane.setBackground(Color.white);
		drawingPane.addMouseMotionListener(this);
		scroller = new JScrollPane(drawingPane);
		add(scroller, "Center");
	}

	public void loadGraph(Vector cfgvector, int numbercfg)
	{
		bpaint = true;
		this.cfgvector = null;
		this.cfgvector = new Vector(cfgvector);
		this.numbercfg = numbercfg;
		vector = new Vector();
		up = new Vector();
		down = new Vector();
		int blocks = 0;
		int res = 0;
		int proc = 0;
		int k = cfgvector.size();
		int j = 0;
		for (int i = 0; i < k; i++)
		{
			Rect rect = new Rect(Upperleft, Upperright + j, Rectwidth, Rectheight);
			vector.add(rect);
			j += Inteval;
			CfgDataStruct cfg = (CfgDataStruct)cfgvector.get(i);
			if (proc != cfg.procnum)
			{
				proc++;
				blocks += res;
				res = 1;
			} else
			{
				res++;
			}
			if (cfg.returnblocka != -1)
				if (cfg.returnblocka <= cfg.blocknum)
					up.add(new Pair(cfg.blocknum + blocks, cfg.returnblocka + blocks));
				else
				if (cfg.returnblocka - cfg.blocknum != 1)
					down.add(new Pair(cfg.blocknum + blocks, cfg.returnblocka + blocks));
			if (cfg.returnblockb != -1)
				if (cfg.returnblockb <= cfg.blocknum)
					up.add(new Pair(cfg.blocknum + blocks, cfg.returnblockb + blocks));
				else
				if (cfg.returnblockb - cfg.blocknum != 1)
					down.add(new Pair(cfg.blocknum + blocks, cfg.returnblockb + blocks));
		}

		Sort(up, true);
		Sort(down, false);
		ActualHeight = j + 200;
	}

	public void drawRect(Graphics2D g, int index)
	{
		Rect rect = (Rect)vector.get(index);
		CfgDataStruct cfg = (CfgDataStruct)cfgvector.get(index);
		String str = "P: " + cfg.getProcnum() + " B: " + cfg.getBlocknum();
		g.drawRect(rect.upperleft, rect.upperright, rect.width, rect.height);
		g.drawString(str, rect.upperleft + 5, rect.upperright + 15);
	}

	public void drawArrowLine(Graphics2D g, int index)
	{
		Rect recta = (Rect)vector.get(index);
		Rect rectb = (Rect)vector.get(index + 1);
		int p = recta.upperleft + recta.width / 2;
		int q = recta.upperright + recta.height;
		int r = rectb.upperright;
		drawArrow(g, new java.awt.geom.Point2D.Double(p, q), new java.awt.geom.Point2D.Double(p, r));
		g.drawLine(p, q, p, r);
	}

	public void drawArrow(Graphics2D g2d, Point2D fromPoint, Point2D toPoint)
	{
		double ARROW_LENGTH = 10D;
		double x1 = fromPoint.getX();
		double y1 = fromPoint.getY();
		double x2 = toPoint.getX();
		double y2 = toPoint.getY();
		double distance = fromPoint.distance(toPoint);
		double cita = Math.toDegrees(Math.asin(Math.abs(y1 - y2) / distance));
		double subCitaSin = ARROW_LENGTH * Math.sin(Math.toRadians(cita - 30D));
		double subCitaCos = ARROW_LENGTH * Math.cos(Math.toRadians(cita - 30D));
		double addCitaSin = ARROW_LENGTH * Math.sin(Math.toRadians(cita + 30D));
		double addCitaCos = ARROW_LENGTH * Math.cos(Math.toRadians(cita + 30D));
		double up1;
		double down1;
		double up2;
		double down2;
		if (x1 > x2)
		{
			if (y1 > y2)
			{
				up1 = x2 + subCitaCos;
				down1 = y2 + subCitaSin;
				up2 = x2 + addCitaCos;
				down2 = y2 + addCitaSin;
			} else
			{
				up1 = x2 + subCitaCos;
				down1 = y2 - subCitaSin;
				up2 = x2 + addCitaCos;
				down2 = y2 - addCitaSin;
			}
		} else
		if (y1 > y2)
		{
			up1 = x2 - subCitaCos;
			down1 = y2 + subCitaSin;
			up2 = x2 - addCitaCos;
			down2 = y2 + addCitaSin;
		} else
		{
			up1 = x2 - subCitaCos;
			down1 = y2 - subCitaSin;
			up2 = x2 - addCitaCos;
			down2 = y2 - addCitaSin;
		}
		g2d.draw(new java.awt.geom.Line2D.Double(new java.awt.geom.Point2D.Double(up1, down1), toPoint));
		g2d.draw(new java.awt.geom.Line2D.Double(new java.awt.geom.Point2D.Double(up2, down2), toPoint));
	}

	public void drawArrowArc(Graphics2D g, int from, int to, int width, boolean bleft)
	{
		int p = 0;
		int q = 0;
		int r = 0;
		Rect rectf = (Rect)vector.get(from);
		Rect rectt = (Rect)vector.get(to);
		if (bleft)
		{
			if (rectf.upperright > rectt.upperright)
			{
				Rect temp = rectf;
				rectf = rectt;
				rectt = temp;
			}
			p = rectf.upperleft - width / 2;
			q = rectf.upperright + Rectheight;
			r = rectt.upperright - rectf.upperright - Rectheight;
			g.drawArc(p, q, width, r, 90, 180);
			drawArrow(g, new java.awt.geom.Point2D.Double(rectt.upperleft - 1, rectt.upperright - 1), new java.awt.geom.Point2D.Double(rectt.upperleft, rectt.upperright));
		} else
		{
			if (rectf.upperright < rectt.upperright)
			{
				Rect temp = rectf;
				rectf = rectt;
				rectt = temp;
			}
			p = (rectf.upperleft + rectf.width) - width / 2;
			q = rectt.upperright;
			r = (rectf.upperright - rectt.upperright) + rectf.height;
			g.drawArc(p, q, width, r, 270, 180);
			drawArrow(g, new java.awt.geom.Point2D.Double(rectt.upperleft + rectt.width + 1, rectt.upperright + 1), new java.awt.geom.Point2D.Double(rectt.upperleft + rectt.width, rectt.upperright));
		}
	}

	public void Sort(Vector vector, boolean b)
	{
		int len = vector.size();
		for (int i = 0; i < len; i++)
		{
			for (int j = i + 1; j < len; j++)
			{
				Pair pairi = (Pair)vector.get(i);
				Pair pairj = (Pair)vector.get(j);
				int disi = pairi.from - pairi.to;
				if (!b)
					disi = -disi;
				int disj = pairj.from - pairj.to;
				if (!b)
					disj = -disj;
				if (disi > disj)
				{
					vector.setElementAt(pairi, j);
					vector.setElementAt(pairj, i);
				}
			}

		}

	}

	public boolean isBpaint()
	{
		return bpaint;
	}

	public void setBpaint(boolean bpaint)
	{
		this.bpaint = bpaint;
	}

	public void mouseDragged(MouseEvent mouseevent)
	{
	}

	public int getIndex(int y, int x)
	{
		if (x < Upperleft || x > Upperleft + Rectwidth)
			return -1;
		int i = y % Inteval;
		if (i <= 40)
		{
			i = y / Inteval;
			if (i < cfgvector.size())
				return i;
		}
		return -1;
	}

	public void mouseMoved(MouseEvent e)
	{
		if (!bpaint)
			return;
		else
			return;
	}

	public boolean isBstop()
	{
		return bstop;
	}

	public void setBstop(boolean bstop)
	{
		this.bstop = bstop;
	}


}
