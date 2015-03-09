// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Shell.java

package src;

import java.io.*;
import java.util.Scanner;

public class Shell
{

	public String ssh;
	public String run_dir;
	public String fail_lp;
	public String gcc;
	public String est_dir;
	public String sim_dir;
	public String sim;
	public String ssh_dir;
	public String rm_dir;
	public String lpsolver_dir;
	public String compile_dir;
	public String simulate_dir;
	public String cplexsolve_dir;
	public String est;
	public String solve;
	public String currentpath;
	public String source_dir;
	public String source_file;
	public String source_binary;
	public String source_lp;
	public String target;
	public String s_compile_err;
	public static String S_ERR = "error";
	public static String default_config = " -cache:il1 il1:16:32:2:l -mem:lat 30 2  -bpred 2lev -bpred:2lev 1 128 2 1  -decode:width 1 -issue:width 1 -commit:width 1 -fetch:ifqsize 4 -ruu:size 8 ";
	public boolean bfail;
	public int rm_length;
	public int compile_length;
	public int simulate_length;

	public String getRun_dir()
	{
		return run_dir;
	}

	public void setRun_dir(String run_dir)
	{
		this.run_dir = run_dir;
		est_dir = run_dir + "/est";
		compile_dir = run_dir + "/gui" + "/compile.sh";
		cplexsolve_dir = run_dir + "/gui" + "/solve.sh";
		rm_dir = run_dir + "/gui" + "/rm.sh";
		simulate_dir = run_dir + "/gui" + "/simulate.sh";
		est = est_dir + "/est";
		setOtherDir();
	}

	public void setOtherDir()
	{
		File config = new File(est_dir + "/configdir.info");
		gcc = "";
		lpsolver_dir = "";
		sim_dir = "";
		if (config.exists())
		{
			Scanner scan = null;
			try
			{
				scan = new Scanner(config);
				if (scan.hasNextLine())
				{
					gcc = scan.nextLine();
					lpsolver_dir = scan.nextLine();
					sim_dir = scan.nextLine();
				}
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			scan.close();
			solve = lpsolver_dir + "/lp_solve";
			sim = sim_dir + "/sim-outorder";
		}
	}

	public void setlpSolverDir(String lp)
	{
		lpsolver_dir = lp;
		solve = lpsolver_dir + "/lp_solve";
		try
		{
			FileWriter writer = new FileWriter(est_dir + "/configdir.info");
			writer.write(gcc + "\n");
			writer.write(lpsolver_dir + "\n");
			writer.write(sim_dir + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setSimplesimDir(String sim)
	{
		sim_dir = sim;
		sim = sim_dir + "/sim-outorder";
		try
		{
			FileWriter writer = new FileWriter(est_dir + "/configdir.info");
			writer.write(gcc + "\n");
			writer.write(lpsolver_dir + "\n");
			writer.write(sim_dir + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setGcc(String gcc)
	{
		this.gcc = gcc;
		try
		{
			FileWriter writer = new FileWriter(est_dir + "/configdir.info");
			writer.write(gcc + "\n");
			writer.write(lpsolver_dir + "\n");
			writer.write(sim_dir + "\n");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getGcc()
	{
		return gcc;
	}

	public Shell()
	{
		ssh = "sh";
		run_dir = "";
		fail_lp = "This problem is infeasible";
		currentpath = "";
		bfail = false;
		rm_length = 3;
		compile_length = 7;
		simulate_length = 6;
	}

	public boolean isDirSet()
	{
		if (gcc == "")
			return false;
		if (lpsolver_dir == "")
			return false;
		return sim_dir != "";
	}

	public void setDir(String dirName, String dirPath)
	{
		source_dir = dirPath;
		target = dirName;
		source_binary = source_dir + "/" + target;
		source_lp = source_binary + ".lp";
	}

	public void setrmCmd(String cmd[])
	{
		cmd[0] = ssh;
		cmd[1] = rm_dir;
		cmd[2] = source_dir;
	}

	public void callrm()
	{
		Process p = null;
		String cmd[] = new String[rm_length];
		setrmCmd(cmd);
		System.out.println("call rm now!");
		try
		{
			p = Runtime.getRuntime().exec(cmd);
			try
			{
				int exitValue = p.waitFor();
				System.out.println("exitValue:" + exitValue);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			DataInputStream ls_in = new DataInputStream(p.getInputStream());
			String ls_str;
			try
			{
				while ((ls_str = ls_in.readLine()) != null) 
					System.out.println(ls_str);
			}
			catch (IOException e)
			{
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setcompileCmd(String cmd[])
	{
		cmd[0] = ssh;
		cmd[1] = compile_dir;
		cmd[2] = source_dir;
		cmd[3] = run_dir;
		cmd[4] = gcc;
		cmd[5] = sim_dir;
		cmd[6] = target;
	}

	public int callCompile()
	{
		Process p = null;
		String cmd[] = new String[compile_length];
		setcompileCmd(cmd);
		System.out.println("call compile now!");
		int exitValue = 0;
		s_compile_err = "";
		try
		{
			p = Runtime.getRuntime().exec(cmd);
			try
			{
				exitValue = p.waitFor();
				System.out.println("exitValue:" + exitValue);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			DataInputStream ls_in = new DataInputStream(p.getInputStream());
			DataInputStream ls_err = new DataInputStream(p.getErrorStream());
			String ls_str;
			try
			{
				while ((ls_str = ls_err.readLine()) != null) 
				{
					System.out.println(ls_str);
					s_compile_err += ls_str;
					s_compile_err += "\n";
				}
			}
			catch (IOException e)
			{
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return exitValue;
	}

	public void setanalyzeCmd(String cmd[], boolean b)
	{
		if (b)
		{
			cmd[0] = est;
			cmd[1] = "-config";
			cmd[2] = est_dir + "/processor.opt";
			cmd[3] = source_binary;
		} else
		{
			cmd[0] = est;
			cmd[1] = source_binary;
		}
	}

	public void setlpCmd(String cmd[])
	{
		cmd[0] = solve;
		cmd[1] = "-rxli";
		cmd[2] = lpsolver_dir + "/xli_CPLEX";
		cmd[3] = source_lp;
	}

	public void setcplexCmd(String cmd[])
	{
		cmd[0] = ssh;
		cmd[1] = cplexsolve_dir;
		cmd[2] = source_binary;
		cmd[3] = source_dir;
	}

	public String callAnalyze()
	{
		System.out.println("call analyze now");
		Process p = null;
		String err = new String();
		err = "";
		String processor = est_dir + "/processor.opt";
		File file = new File(processor);
		String cmd[];
		if (file.exists())
		{
			cmd = new String[4];
			setanalyzeCmd(cmd, true);
			System.out.println("estimate with config");
		} else
		{
			cmd = new String[4];
			setanalyzeCmd(cmd, true);
			System.out.println("estimate with default config");
			try
			{
				FileWriter writer = new FileWriter(processor);
				writer.write(default_config);
				writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("generate ilp file now!");
		try
		{
			p = Runtime.getRuntime().exec(cmd);
			try
			{
				int exitValue = p.waitFor();
				System.out.println("exitValue:" + exitValue);
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
			DataInputStream ls_in = new DataInputStream(p.getInputStream());
			DataInputStream ls_err = new DataInputStream(p.getErrorStream());
			String ls_str;
			try
			{
				while ((ls_str = ls_err.readLine()) != null) 
				{
					System.out.println(ls_str);
					err = err + ls_str;
				}
				if (err != "")
					return err;
			}
			catch (IOException e)
			{
				System.exit(0);
			}
			try
			{
				while ((ls_str = ls_in.readLine()) != null) 
					System.out.println(ls_str);
			}
			catch (IOException e)
			{
				System.exit(0);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("ilp file has been generated!");
		System.out.println("call lp_solve now!");
		cmd = (String[])null;
		cmd = new String[4];
		p = null;
		setlpCmd(cmd);
		try
		{
			p = Runtime.getRuntime().exec(cmd);
			DataInputStream ls_in = new DataInputStream(p.getInputStream());
			Scanner scanner = new Scanner(ls_in);
			int index = 1;
			String wcet = "0";
			String bm = "0";
			String cm = "0";
			String line = "";
			String key = "";
			System.out.println("finding obj");
			for (; scanner.hasNextLine(); index++)
			{
				line = scanner.nextLine();
				line = line.trim();
				if (index != 2)
					continue;
				err = line;
				break;
			}

			if (!scanner.hasNextLine())
			{
				bfail = true;
				return err;
			}
			bfail = false;
			line = scanner.nextLine();
			System.out.println(line);
			Scanner s = new Scanner(line);
			s.next();
			s.next();
			s.next();
			s.next();
			wcet = s.next();
			s.close();
			while (scanner.hasNext()) 
			{
				key = scanner.next();
				if (key.equals("bm"))
					bm = scanner.next();
				else
				if (key.equals("cm"))
					cm = scanner.next();
			}
			System.out.println("wcet :" + wcet);
			System.out.println("bm :" + bm);
			System.out.println("cm :" + cm);
			err = err + wcet;
			err = err + " " + bm;
			err = err + " " + cm;
			scanner.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("analyze finish!");
		return err;
	}

	public String setsimulateCmd()
	{
		String config = "";
		String processor = est_dir + "/processor.opt";
		File file = new File(processor);
		if (file.exists())
			try
			{
				for (Scanner scan = new Scanner(file); scan.hasNextLine();)
					config = config + scan.nextLine() + " ";

				config = config + " -res:ialu 2 -res:memport 2 ";
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		else
			config = default_config;
		String command = "";
		command = command + sim_dir + "/sim-outorder";
		command = command + " " + config;
		command = command + " " + source_dir + "/" + target;
		command = command + " > ";
		command = command + " " + source_dir + "/" + target + ".sim";
		return command;
	}

	public String callSimulate()
	{
		System.out.println("call simulate now");
		String result = new String();
		result = "";
		Process p = null;
		String wcet = "";
		String bm = "";
		String cm = "";
		String key = "";
		String command = "";
		command = setsimulateCmd();
		try
		{
			p = Runtime.getRuntime().exec(command);
			DataInputStream ls_in = new DataInputStream(p.getInputStream());
			DataInputStream ls_err = new DataInputStream(p.getErrorStream());
			Scanner scan;
			for (scan = new Scanner(ls_in); scan.hasNext();)
			{
				key = scan.next();
				if (key.equals("cycles:"))
					wcet = scan.next();
				if (key.equals("misses:"))
					if (bm.equals(""))
						bm = scan.next();
					else
						cm = scan.next();
			}

			scan.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		result = wcet + " " + bm + " " + cm;
		System.out.println("simulate finish!");
		if (wcet.equals("") || bm.equals("") || cm.equals(""))
		{
			return S_ERR;
		} else
		{
			System.out.println("result: " + result);
			return result;
		}
	}

	public void setSource(String dir)
	{
		int index = dir.length() - 1;
		int end = -1;
		for (; dir.charAt(index) != '/'; index--)
			if (end == -1 && dir.charAt(index) == '.')
				end = index;

		source_dir = dir.substring(0, index);
		source_file = dir.substring(index + 1, end);
		source_binary = dir.substring(0, end);
		source_lp = source_binary + ".lp";
	}

}
