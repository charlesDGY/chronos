// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   InputDependent.java

package src;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

// Referenced classes of package src:
//			CfgDataStruct, Utils

public class InputDependent
{
	public class symbol
	{

		int offset;
		int size;
		int esize;
		String type;
		String name;
		String function;

		public symbol()
		{
		}
	}

	public class pair
	{

		public int base;
		public int offset;
		public int size;

		public pair(int base, int offset)
		{
			this.base = base;
			this.offset = offset;
			size = 0;
		}
	}

	public class code
	{

		String adr;
		String inst;
		String opr1;
		String opr2;
		String opr3;

		public code()
		{
			opr1 = "";
			opr2 = "";
			opr3 = "";
		}
	}

	public class procedure
	{
		public class block
		{

			public int bid;
			public int begin;
			public int end;
			public Vector prev;
			public Vector succ;
			public int aprev[];
			public int regsize;
			public int prevsize;
			public boolean binput[];
			public boolean boutput[];
			public Vector spin;
			public Vector spout;
			public int bn;
			public int pn;
			public boolean bchange;
			public boolean bdepend;
			public boolean idepend;
			public int instval;
			public boolean bloop;
			public int spstart;
			public int funindex;

			public void trans()
				throws Exception
			{
				bchange = false;
				bdepend = false;
				if (bn != 0)
					union();
				for (int i = begin; i <= end; i++)
					if (bcodes[i])
						doinst(i);

				dochange();
			}

			public void dochange()
			{
				for (int i = 0; i < regsize; i++)
				{
					if (binput[i] != boutput[i])
						bchange = true;
					boutput[i] = binput[i];
				}

			}

			public void doinst(int i)
				throws Exception
			{
				int inst = Utils.GetInstIndex(acodes[i].inst);
				instval = inst;
				switch (inst)
				{
				case 0: // '\0'
				case 1: // '\001'
				case 2: // '\002'
				case 3: // '\003'
				case 4: // '\004'
				case 5: // '\005'
				case 6: // '\006'
				case 7: // '\007'
				case 8: // '\b'
				case 9: // '\t'
					doload(i);
					break;

				case 10: // '\n'
				case 11: // '\013'
				case 12: // '\f'
				case 13: // '\r'
				case 14: // '\016'
				case 15: // '\017'
					doIntAddSub(i);
					break;

				case 16: // '\020'
				case 17: // '\021'
				case 18: // '\022'
				case 19: // '\023'
					doFloatAddSub(i);
					break;

				case 20: // '\024'
				case 21: // '\025'
				case 22: // '\026'
				case 23: // '\027'
				case 24: // '\030'
				case 25: // '\031'
				case 26: // '\032'
				case 27: // '\033'
				case 28: // '\034'
					dostore(i, inst);
					break;

				case 29: // '\035'
				case 30: // '\036'
				case 31: // '\037'
				case 32: // ' '
				case 33: // '!'
				case 34: // '"'
				case 35: // '#'
					dological(i);
					break;

				case 36: // '$'
				case 37: // '%'
				case 38: // '&'
				case 39: // '\''
				case 40: // '('
				case 41: // ')'
					doshift(i);
					break;

				case 42: // '*'
				case 43: // '+'
				case 44: // ','
				case 45: // '-'
					dosetreg(i);
					break;

				case 46: // '.'
				case 47: // '/'
				case 48: // '0'
				case 49: // '1'
					doIntMulDiv(i);
					break;

				case 50: // '2'
				case 51: // '3'
				case 52: // '4'
				case 53: // '5'
					doMoveHiLo(i, inst);
					break;

				case 54: // '6'
				case 55: // '7'
				case 56: // '8'
				case 57: // '9'
					doFloatMulDiv(i);
					break;

				case 58: // ':'
				case 59: // ';'
				case 60: // '<'
				case 61: // '='
				case 62: // '>'
				case 63: // '?'
				case 64: // '@'
				case 65: // 'A'
				case 66: // 'B'
				case 67: // 'C'
				case 68: // 'D'
				case 69: // 'E'
					doFloatTrans(i);
					break;

				case 70: // 'F'
				case 71: // 'G'
				case 72: // 'H'
				case 73: // 'I'
				case 74: // 'J'
				case 75: // 'K'
					doFloatTest(i);
					break;

				case 76: // 'L'
				case 77: // 'M'
					doFloatSquare(i);
					break;

				case 78: // 'N'
				case 79: // 'O'
					doMiscellaneous(i, inst);
					break;

				case 80: // 'P'
				case 81: // 'Q'
				case 82: // 'R'
				case 83: // 'S'
				case 84: // 'T'
				case 85: // 'U'
				case 86: // 'V'
				case 87: // 'W'
					dobranch(i, inst);
					break;

				case 88: // 'X'
					dolui(i);
					break;

				case 100: // 'd'
					dofunctions(i);
					break;

				case 200: 
					dojr(i);
					break;
				}
			}

			public void dojr(int index)
				throws Exception
			{
				if (acodes[index].opr1.equals("$31") || acodes[index].opr1.equals("$2"))
					return;
				if (!acodes[index].opr1.equals("$31"))
					throw new Exception();
				else
					return;
			}

			public void dofunctions(int index)
				throws Exception
			{
				if (acodes[index].opr2.equals("<scanf>"))
				{
					doscanf(index);
					return;
				}
				if (acodes[index].opr2.equals("<fread>"))
				{
					dofread(index);
					return;
				}
				int ifun = GetFunIndex(acodes[index].opr2);
				funindex = ifun;
				if (ifun == pn)
					return;
				if (ifun != -1)
				{
					procedure p = procs[ifun];
					for (int i = 0; i < regsize; i++)
						p.binput[i] = binput[i];

					int sp = regval[29];
					regval[29] -= basesp;
					p.DataFlowAnalysis();
					regval[29] = sp;
					for (int i = 0; i < regsize; i++)
						binput[i] = p.binput[i];

				}
			}

			public void dofgetc()
			{
				binput[2] = true;
			}

			public void dolui(int index)
			{
				int reg1 = Utils.GetRegIndex(acodes[index].opr1);
				int reg2 = Getnumber(acodes[index].opr2);
				regval[reg1] = reg2 << 16;
				bgp[reg1] = true;
				binput[reg1] = false;
			}

			public void dobranch(int index, int inst)
				throws Exception
			{
				boolean b = false;
				switch (inst)
				{
				case 80: // 'P'
				case 81: // 'Q'
				{
					int reg1 = Utils.GetRegIndex(acodes[index].opr1);
					int reg2 = Utils.GetRegIndex(acodes[index].opr2);
					if (binput[reg1] || binput[reg2])
						b = true;
					break;
				}

				case 82: // 'R'
				case 83: // 'S'
				case 84: // 'T'
				case 85: // 'U'
				{
					int reg1 = Utils.GetRegIndex(acodes[index].opr1);
					if (binput[reg1])
						b = true;
					// fall through
				}

				case 86: // 'V'
				case 87: // 'W'
				{
					if (binput[66])
						b = true;
					break;
				}
				}
				if (!bdepend)
					bdepend = b;
				if (bloop)
					return;
				if (bloop && b)
				{
					int bi = GetBlockIndex(GetCodeIndex(acodes[index].adr));
					if (blocks[bi].spstart != -1)
					{
						int i = blocks[bi].spstart;
						int j = ((pair)spin.get(i)).offset;
						int len = symbols.size();
						int size = 0;
						int esize = 0;
						for (i = 0; i < len; i++)
						{
							symbol s = (symbol)symbols.get(i);
							if (s.offset != j)
								continue;
							size = s.size;
							esize = s.esize;
							break;
						}

						for (i = 0; i < size; i += esize)
						{
							pair p = new pair(29, i + j);
							if (!contain(spin, p))
								spin.add(p);
						}

					}
				}
				if (bloop && !b)
				{
					System.out.println("loop");
					int begin = 0;
					int end = index - 1;
					switch (instval)
					{
					default:
						break;

					case 80: // 'P'
					{
						int reg1 = Utils.GetRegIndex(acodes[index].opr1);
						int reg2 = Utils.GetRegIndex(acodes[index].opr2);
						begin = GetCodeIndex(acodes[index].opr3);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] == regval[reg2]);
						break;
					}

					case 81: // 'Q'
					{
						int reg1 = Utils.GetRegIndex(acodes[index].opr1);
						int reg2 = Utils.GetRegIndex(acodes[index].opr2);
						begin = GetCodeIndex(acodes[index].opr3);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] != regval[reg2]);
						break;
					}

					case 82: // 'R'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] <= 0);
						break;
					}

					case 83: // 'S'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] > 0);
						break;
					}

					case 84: // 'T'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] < 0);
						break;
					}

					case 85: // 'U'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[reg1] >= 0);
						break;
					}

					case 86: // 'V'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[66] != 1);
						break;
					}

					case 87: // 'W'
					{
						int reg1 = Utils.GetInstIndex(acodes[index].opr1);
						begin = GetCodeIndex(acodes[index].opr2);
						do
						{
							for (int i = begin; i <= end; i++)
								if (bcodes[i])
									doinst(i);

						} while (regval[66] != -1);
						break;
					}
					}
				}
			}

			public void doMiscellaneous(int index, int inst)
			{
				int reg1 = Utils.GetRegIndex(acodes[index].opr1);
				int reg2 = Utils.GetRegIndex(acodes[index].opr2);
				switch (inst)
				{
				case 78: // 'N'
					binput[reg1] = binput[reg2];
					break;

				case 79: // 'O'
					binput[reg2] = binput[reg1];
					break;
				}
			}

			public void doFloatSquare(int index)
			{
				int regto = Utils.GetRegIndex(acodes[index].opr1);
				int regfrom = Utils.GetRegIndex(acodes[index].opr2);
				binput[regto] = binput[regfrom];
			}

			public void doFloatTest(int index)
			{
				int regfrom1 = Utils.GetRegIndex(acodes[index].opr1);
				int regfrom2 = Utils.GetRegIndex(acodes[index].opr2);
				boolean b = false;
				if (binput[regfrom1] || binput[regfrom2])
					b = true;
				binput[66] = b;
			}

			public void doFloatTrans(int index)
			{
				int regto = Utils.GetRegIndex(acodes[index].opr1);
				int regfrom = Utils.GetRegIndex(acodes[index].opr2);
				binput[regto] = binput[regfrom];
			}

			public void doFloatMulDiv(int index)
			{
				doFloatAddSub(index);
			}

			public void doMoveHiLo(int index, int inst)
			{
				int reg = Utils.GetRegIndex(acodes[index].opr1);
				switch (inst)
				{
				case 50: // '2'
					binput[reg] = binput[32];
					break;

				case 51: // '3'
					binput[32] = binput[reg];
					break;

				case 52: // '4'
					binput[reg] = binput[33];
					break;

				case 53: // '5'
					binput[33] = binput[reg];
					break;
				}
			}

			public void doIntMulDiv(int index)
			{
				boolean b = false;
				if (acodes[index].opr1.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr1);
					b = binput[regfrom];
				}
				if (acodes[index].opr2.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr2);
					if (!b && binput[regfrom])
						b = true;
				}
				binput[32] = binput[33] = b;
			}

			public void dosetreg(int index)
			{
				int regdest = Utils.GetRegIndex(acodes[index].opr1);
				boolean b = false;
				int vala = 0;
				int valb = 0;
				if (acodes[index].opr2.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr2);
					b = binput[regfrom];
					vala = regval[regfrom];
				} else
				{
					vala = Getnumber(acodes[index].opr2);
				}
				if (acodes[index].opr3.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr3);
					if (!b && binput[regfrom])
						b = true;
					valb = regval[regfrom];
				} else
				{
					valb = Getnumber(acodes[index].opr3);
				}
				binput[regdest] = b;
				switch (instval)
				{
				case 42: // '*'
				case 43: // '+'
					regval[regdest] = vala >= valb ? 0 : 1;
					break;

				case 44: // ','
				case 45: // '-'
					regval[regdest] = (long)vala >= (long)valb ? 0 : 1;
					break;
				}
			}

			public void doshift(int index)
			{
				int regdest = Utils.GetRegIndex(acodes[index].opr1);
				boolean b = false;
				int vala = 0;
				int valb = 0;
				if (acodes[index].opr2.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr2);
					b = binput[regfrom];
					vala = regval[regfrom];
				} else
				{
					vala = GetHexnumber(acodes[index].opr2);
				}
				if (acodes[index].opr3.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr3);
					if (!b && binput[regfrom])
						b = true;
					valb = regval[regfrom];
				} else
				{
					valb = GetHexnumber(acodes[index].opr3);
				}
				binput[regdest] = b;
				if (b)
					return;
				switch (instval)
				{
				case 36: // '$'
					regval[regdest] = vala << valb;
					break;

				case 37: // '%'
					regval[regdest] = vala << (regval[valb] & 0x1f);
					break;

				case 38: // '&'
					regval[regdest] = vala >> valb;
					break;

				case 39: // '\''
					regval[regdest] = vala >> (regval[valb] & 0x1f);
					break;
				}
			}

			public void dological(int index)
			{
				doFloatAddSub(index);
			}

			public void dostore(int index, int inst)
			{
				boolean breturn = false;
				switch (inst)
				{
				case 24: // '\030'
					breturn = true;
					break;
				}
				if (breturn)
					return;
				if (!breturn)
					return;
				int base = 0;
				int regfrom = Utils.GetRegIndex(acodes[index].opr1);
				int offset = Getoffset(acodes[index].opr2);
				String reg = Getreg(acodes[index].opr2);
				int regto = Utils.GetRegIndex(reg);
				boolean b = binput[regfrom];
				if (bsp[regto])
					base = 29;
				else
				if (bgp[regto])
					base = 28;
				pair p = new pair(base, offset + regval[regto]);
				if (base == 29)
				{
					if (b && !contain(spin, p))
						spin.add(p);
					if (!b && contain(spin, p))
						remove(spin, p);
				} else
				{
					if (b && !contain(gbspin, p))
						spin.add(p);
					if (!b && contain(gbspin, p))
						remove(spin, p);
				}
			}

			public void doFloatAddSub(int index)
			{
				int regdest = Utils.GetRegIndex(acodes[index].opr1);
				boolean b = false;
				if (acodes[index].opr2.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr2);
					b = binput[regfrom];
				}
				if (acodes[index].opr3.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr3);
					if (!b && binput[regfrom])
						b = true;
				}
				binput[regdest] = b;
			}

			public void doIntAddSub(int index)
			{
				int regdest = Utils.GetRegIndex(acodes[index].opr1);
				boolean b = false;
				boolean bp = false;
				boolean bpg = false;
				int vala;
				if (acodes[index].opr2.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr2);
					b = binput[regfrom];
					vala = regval[regfrom];
					bp = bsp[regfrom];
					bpg = bgp[regfrom];
				} else
				{
					vala = Getnumber(acodes[index].opr2);
				}
				int valb;
				if (acodes[index].opr3.charAt(0) == '$')
				{
					int regfrom = Utils.GetRegIndex(acodes[index].opr3);
					if (!b && binput[regfrom])
						b = true;
					valb = regval[regfrom];
					if (!bp && bsp[regfrom])
						bp = true;
					if (!bpg && bgp[regfrom])
						bpg = true;
				} else
				{
					valb = Getnumber(acodes[index].opr3);
				}
				binput[regdest] = b;
				bsp[regdest] = bp;
				bgp[regdest] = bpg;
				if (b)
				{
					regval[regdest] = 0;
					return;
				}
				switch (instval)
				{
				default:
					break;

				case 10: // '\n'
				case 11: // '\013'
				case 12: // '\f'
				case 13: // '\r'
					if (regdest == 29)
						basesp = vala + valb;
					regval[regdest] = vala + valb;
					break;

				case 14: // '\016'
				case 15: // '\017'
					if (regdest == 29)
						basesp = vala + valb;
					regval[regdest] = vala - valb;
					break;
				}
			}

			public void doload(int index)
			{
				if (acodes[index].opr3.equals(""))
				{
					String reg = acodes[index].opr2;
					int offset = Getoffset(reg);
					int base = 0;
					boolean b = false;
					reg = Getreg(reg);
					int regfrom = Utils.GetRegIndex(reg);
					int regto = Utils.GetRegIndex(acodes[index].opr1);
					binput[regto] = true;
				}
			}

			public void dofread(int index)
			{
				int line1 = ((Integer)vlines.get(index)).intValue();
				index--;
				int line2 = 0;
				for (; index >= 0; index--)
				{
					line2 = ((Integer)vlines.get(index)).intValue();
					if ((line1 == line2 || acodes[line1].adr.equals(acodes[line2].adr)) && bcodes[index])
						break;
				}

				for (; index >= 0 && bcodes[index]; index--)
				{
					if (Utils.GetRegIndex(acodes[index].opr1) != 4)
						continue;
					int l2 = Utils.GetRegIndex(acodes[index].opr2);
					int l3 = Utils.GetRegIndex(acodes[index].opr3);
					int l4 = 0;
					if (l2 != -1 && bsp[l2])
						l4 = 29;
					else
					if (l3 != -1 && bsp[l3])
						l4 = 29;
					else
					if (l2 != -1 && bgp[l2])
						l4 = 28;
					else
					if (l3 != -1 && bgp[l3])
						l4 = 28;
					break;
				}

				binput[2] = true;
			}

			public void spinadd(int offset, int base)
			{
				if (base == 29)
				{
					int i = LookupSymbolTable(offset, name.substring(1, name.length() - 1));
					if (i != -1)
					{
						String type = ((symbol)symbols.get(i)).type;
						if (type.equals("array"))
						{
							int size = ((symbol)symbols.get(i)).size;
							int esize = ((symbol)symbols.get(i)).esize;
							pair p = new pair(base, offset);
							p.size = size;
							if (!contain(spin, p))
								spin.add(p);
							return;
						}
					}
				}
				if (base == 28)
				{
					int i = LookupGbSymbolTable(offset);
					if (i != -1)
					{
						String type = ((symbol)pasymbols.get(i)).type;
						if (type.equals("array"))
						{
							int size = ((symbol)pasymbols.get(i)).size;
							int esize = ((symbol)pasymbols.get(i)).esize;
							pair p = new pair(base, offset);
							p.size = size;
							if (!contain(gbspin, p))
								gbspin.add(p);
							return;
						}
					}
				}
				pair p = new pair(base, offset);
				if (base == 29)
				{
					if (!contain(spin, p))
						spin.add(p);
				} else
				if (!contain(gbspin, p))
					gbspin.add(p);
			}

			public void doscanf(int index)
			{
				int line1 = ((Integer)vlines.get(index)).intValue();
				index--;
				int line2 = 0;
				for (; index >= 0; index--)
				{
					line2 = ((Integer)vlines.get(index)).intValue();
					if ((line1 == line2 || acodes[line1].adr.equals(acodes[line2].adr)) && bcodes[index])
						break;
				}

				while (index >= 0) 
				{
					line2 = ((Integer)vlines.get(index)).intValue();
					int op1num = Utils.GetRegIndex(acodes[index].opr1);
					if ((op1num == 5 || op1num == 6 || op1num == 7 || op1num == 2) && (line1 == line2 || acodes[line1].adr.equals(acodes[line2].adr)) && bcodes[index])
						if (acodes[index].inst.equals("addiu") || acodes[index].inst.equals("addi"))
						{
							int l1 = Utils.GetRegIndex(acodes[index].opr1);
							int l2 = Utils.GetRegIndex(acodes[index].opr2);
							int l3 = Utils.GetRegIndex(acodes[index].opr3);
							int l4;
							if (bsp[l2])
								l4 = 29;
							else
							if (bgp[l2])
							{
								l4 = 28;
							} else
							{
								index--;
								continue;
							}
							if (l3 == -1 && l2 != 0)
								l3 = Integer.parseInt(acodes[index].opr3);
						} else
						if (acodes[index].inst.equals("addu") || acodes[index].inst.equals("add"))
						{
							int l1 = Utils.GetRegIndex(acodes[index].opr1);
							int l2 = Utils.GetRegIndex(acodes[index].opr2);
							int l3 = Utils.GetRegIndex(acodes[index].opr3);
							int l4;
							if (bsp[l2] || bsp[l3])
								l4 = 29;
							else
							if (bgp[l2] || bgp[l3])
							{
								l4 = 28;
							} else
							{
								index--;
								continue;
							}
						}
					index--;
				}
				binput[2] = true;
			}

			public void union()
			{
				for (int i = 0; i < regsize; i++)
				{
					boolean b = false;
					for (int j = 0; j < prevsize; j++)
					{
						if (!blocks[aprev[j]].binput[i])
							continue;
						b = true;
						break;
					}

					binput[i] = b;
				}

			}

			public int GetBlockIndex(int line)
			{
				int i;
				for (i = 0; i < numb; i++)
					if (blocks[i].begin <= line && blocks[i].end >= line)
						break;

				return i;
			}

			public boolean contain(Vector v, pair p)
			{
				int j = v.size();
				for (int i = 0; i < j; i++)
				{
					if (((pair)v.get(i)).size == 0 && ((pair)v.get(i)).base == p.base && ((pair)v.get(i)).offset == p.offset)
						return true;
					if (((pair)v.get(i)).size > 0)
					{
						if (((pair)v.get(i)).base == 0x10001900)
							System.out.println(((pair)v.get(i)).offset);
						if (p.base == ((pair)v.get(i)).base && p.offset >= ((pair)v.get(i)).offset && p.offset < ((pair)v.get(i)).offset + ((pair)v.get(i)).size)
							return true;
					}
				}

				return false;
			}

			public void remove(Vector v, pair p)
			{
				int j = v.size();
				for (int i = 0; i < j; i++)
					if (((pair)v.get(i)).base == p.base && ((pair)v.get(i)).offset == p.offset && p.size == 0)
					{
						v.remove(i);
						return;
					}

			}

			public block()
			{
				spstart = 0;
				funindex = -1;
				binput = new boolean[InputDependent.RegSize];
				boutput = new boolean[InputDependent.RegSize];
				prev = new Vector();
				succ = new Vector();
				spin = new Vector();
				spout = new Vector();
				prevsize = 0;
				spstart = -1;
				idepend = false;
			}
		}


		public String name;
		public int basesp;
		public int pn;
		public int numb;
		public block blocks[];
		public boolean binput[];
		public boolean boutput[];

		public void init()
		{
			int len = InputDependent.RegSize;
			regval = new int[InputDependent.RegSize];
			for (int i = 0; i < len; i++)
				regval[i] = 0;

			bsp = new boolean[InputDependent.RegSize];
			for (int i = 0; i < len; i++)
				bsp[i] = false;

			bsp[29] = true;
			bgp = new boolean[InputDependent.RegSize];
			for (int i = 0; i < len; i++)
				bgp[i] = false;

			bgp[28] = true;
			initgp();
			for (int i = 0; i < len; i++)
				blocks[0].binput[i] = binput[i];

		}

		public void dogp(code c)
		{
			int inst = Utils.GetInstIndex(c.inst);
			int imm = 0;
			int opr2 = 0;
			switch (inst)
			{
			case 88: // 'X'
				imm = Getnumber(c.opr2);
				regval[28] = imm << 16;
				break;

			case 13: // '\r'
				opr2 = Utils.GetRegIndex(c.opr2);
				imm = Getnumber(c.opr3);
				regval[28] = regval[opr2] + imm;
				break;
			}
		}

		public void initgp()
		{
			int len = initcodes.size();
			for (int index = 0; index < len; index++)
			{
				code c = (code)initcodes.get(index);
				if (Utils.GetRegIndex(c.opr1) == 28)
					dogp(c);
			}

		}

		public void DataFlowAnalysis()
			throws Exception
		{
			boolean bchange = true;
			while (bchange) 
			{
				bchange = false;
				init();
				for (int i = 0; i < numb; i++)
				{
					blocks[i].trans();
					if (blocks[i].bchange)
						bchange = true;
				}

			}
			for (int i = 0; i < InputDependent.RegSize; i++)
				binput[i] = blocks[numb - 1].binput[i];

		}

		public void clearinput()
		{
			for (int i = 0; i < InputDependent.RegSize; i++)
				binput[i] = false;

		}


		public procedure()
		{
			binput = new boolean[InputDependent.RegSize];
			boutput = new boolean[InputDependent.RegSize];
			for (int i = 0; i < InputDependent.RegSize; i++)
			{
				binput[i] = false;
				boutput[i] = false;
			}

		}

		public procedure(boolean b[])
		{
			binput = new boolean[InputDependent.RegSize];
			boutput = new boolean[InputDependent.RegSize];
			for (int i = 0; i < InputDependent.RegSize; i++)
			{
				binput[i] = b[i];
				boutput[i] = false;
			}

		}
	}


	public static String over = "end_addr";
	public static String start = "start_addr";
	public static int RegSize = 70;
	public String disfile;
	public String filename;
	public String infofile;
	public String cFiles[];
	public String symbolfile;
	public String simfile;
	public Vector vcodes;
	public boolean bcodes[];
	public code acodes[];
	public int regval[];
	public boolean bsp[];
	public boolean bgp[];
	public procedure procs[];
	public int nump;
	public Vector fnames;
	public Vector vlines;
	public Vector symbols;
	public Vector pasymbols;
	public int alines[];
	public Vector initcodes;
	public Vector gbspin;
	public Vector vecinput;

	public void DataFlowAnalysis()
		throws Exception
	{
		int plen = procs.length;
		vecinput = new Vector();
		for (int i = 0; i < plen; i++)
			if (procs[i].name.equals("<main>"))
				procs[i].DataFlowAnalysis();

		for (int i = 0; i < plen; i++)
		{
			Vector vec = new Vector();
			int k = procs[i].blocks.length;
			for (int j = 0; j < k; j++)
				if (procs[i].blocks[j].bdepend)
					vec.add(Integer.valueOf(j));

			vecinput.add(vec);
		}

		modify();
		SetInputDependent();
		SetCallGraphInputDependent();
	}

	public void modify()
	{
		int j = procs.length;
		for (int i = 0; i < j; i++)
		{
			int j1 = procs[i].blocks.length;
			for (int i1 = 0; i1 < j1; i1++)
				procs[i].blocks[i1].bdepend = false;

		}

		j = vecinput.size();
		for (int i = 0; i < j; i++)
		{
			Vector vec = (Vector)vecinput.get(i);
			int j1 = vec.size();
			for (int i1 = 0; i1 < j1; i1++)
			{
				int k = ((Integer)vec.get(i1)).intValue();
				procs[i].blocks[k].bdepend = true;
			}

		}

	}

	public void SetCallGraphInputDependent()
	{
		int plen = procs.length;
		boolean b = true;
		while (b) 
		{
			b = false;
			for (int i = 0; i < plen; i++)
			{
				for (int j = 0; j < procs[i].numb; j++)
					if (procs[i].blocks[j].idepend)
					{
						int ifun = procs[i].blocks[j].funindex;
						if (ifun > -1)
						{
							for (int k = 0; k < procs[ifun].numb; k++)
								if (!procs[ifun].blocks[k].idepend)
								{
									procs[ifun].blocks[k].idepend = true;
									b = true;
								}

						}
					}

			}

		}
	}

	public void SetInputDependent()
	{
		int plen = procs.length;
		boolean b = true;
		while (b) 
		{
			b = false;
			for (int i = 0; i < plen; i++)
			{
				for (int j = 0; j < procs[i].numb; j++)
					if (procs[i].blocks[j].bdepend)
					{
						SetBlock(i, j);
						b = true;
					}

			}

		}
	}

	public void SetBlock(int pi, int bi)
	{
		procs[pi].blocks[bi].bdepend = false;
		int size = procs[pi].blocks[bi].succ.size();
		if (procs[pi].blocks[bi].bloop)
		{
			int ra;
			if (size > 0)
				ra = ((Integer)procs[pi].blocks[bi].succ.get(0)).intValue();
			else
				return;
			if (ra <= bi)
			{
				for (int i = ra; i <= bi; i++)
				{
					procs[pi].blocks[i].idepend = true;
					if (procs[pi].blocks[i].bloop && !procs[pi].blocks[i].idepend && i != bi)
						procs[pi].blocks[i].bdepend = true;
				}

			}
			int rb;
			if (size > 1)
				rb = ((Integer)procs[pi].blocks[bi].succ.get(1)).intValue();
			else
				return;
			if (rb <= bi)
			{
				for (int i = rb; i <= bi; i++)
				{
					procs[pi].blocks[i].idepend = true;
					if (procs[pi].blocks[i].bloop && !procs[pi].blocks[i].idepend && i != bi)
						procs[pi].blocks[i].bdepend = true;
				}

			}
		} else
		{
			int j = GetBranchEnd(pi, bi);
			for (int i = bi + 1; i < j; i++)
			{
				procs[pi].blocks[i].idepend = true;
				if (procs[pi].blocks[i].bloop)
					procs[pi].blocks[i].bdepend = true;
			}

		}
	}

	public int GetBranchEnd(int pi, int bi)
	{
		Vector v = new Vector();
		int arg;
		for (int ra = ((Integer)procs[pi].blocks[bi].succ.get(0)).intValue(); ra < procs[pi].numb; ra = arg)
		{
			v.add(Integer.valueOf(ra));
			if (procs[pi].blocks[ra].succ.size() == 0)
				break;
			int size = procs[pi].blocks[ra].succ.size();
			arg = -1;
			if (size > 0)
				arg = ((Integer)procs[pi].blocks[ra].succ.get(0)).intValue();
			if (arg < ra && size > 1)
				arg = ((Integer)procs[pi].blocks[ra].succ.get(1)).intValue();
			if (arg < ra)
				break;
		}

		int rb = ((Integer)procs[pi].blocks[bi].succ.get(1)).intValue();
		int len = v.size();
		do
		{
			int i;
			for (i = 0; i < len; i++)
				if (((Integer)v.get(i)).intValue() == rb)
					break;

			if (i >= len)
			{
				int size = procs[pi].blocks[rb].succ.size();
			    arg = -1;
				if (size > 0)
					arg = ((Integer)procs[pi].blocks[rb].succ.get(0)).intValue();
				if (arg < rb && size > 1)
					arg = ((Integer)procs[pi].blocks[rb].succ.get(1)).intValue();
				if (arg < rb)
					return procs[pi].numb - 1;
				rb = arg;
			} else
			{
				return rb;
			}
		} while (true);
	}

	public int GetHexnumber(String str)
	{
		String s = str.substring(2);
		return Integer.parseInt(s, 16);
	}

	public int Getnumber(String str)
	{
		int number = 0;
		int len = str.length();
		int index = 0;
		boolean minus = false;
		if (str.charAt(0) == '-')
		{
			minus = true;
			index++;
		}
		for (; index < len; index++)
			number = (number * 10 + str.charAt(index)) - 48;

		if (minus)
			number = -number;
		return number;
	}

	public int LookupGbSymbolTable(int offset)
	{
		int len = pasymbols.size();
		for (int index = 0; index < len; index++)
		{
			symbol s = (symbol)pasymbols.get(index);
			if (s.offset == offset)
				return index;
		}

		return -1;
	}

	public InputDependent(String dir)
	{
		disfile = dir + ".dis";
		regval = new int[RegSize];
		for (int i = 0; i < RegSize; i++)
			regval[i] = 0;

		bsp = new boolean[RegSize];
		for (int i = 0; i < RegSize; i++)
			bsp[i] = false;

		bsp[29] = true;
		bgp = new boolean[RegSize];
		for (int i = 0; i < RegSize; i++)
			bgp[i] = false;

		bgp[28] = true;
		gbspin = new Vector();
	}

	public int Getoffset(String str)
	{
		int len = str.length();
		int index = 0;
		int offset = 0;
		int minus = 1;
		if (str.charAt(0) == '-')
		{
			minus = -1;
			index++;
		}
		for (; isDigit(str.charAt(index)); index++)
			offset = (offset * 10 + str.charAt(index)) - 48;

		return offset * minus;
	}

	public String Getreg(String str)
	{
		int len = str.length();
		int index = 0;
		int offset = 0;
		if (str.charAt(0) == '-')
			index++;
		for (; isDigit(str.charAt(index)); index++)
			offset = (offset * 10 + str.charAt(index)) - 48;

		return str.substring(index + 1, len - 1);
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

	public boolean isSourceLine(String str)
	{
		int nfile = cFiles.length;
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
					return true;
			}
		}

		return false;
	}

	public boolean isValidFunName(String str)
	{
		int len = str.length();
		if (str.charAt(0) == '<' && str.charAt(len - 1) == '>')
		{
			for (int i = 1; i < len - 2; i++)
				if (str.charAt(i) == '+')
					return false;

			return true;
		} else
		{
			return false;
		}
	}

	public void ReadCodes()
	{
		File file = new File(disfile);
		String snames = "";
		Scanner scanner = null;
		boolean b = true;
		while (b) 
			try
			{
				b = false;
				scanner = new Scanner(file);
				if (scanner.findInLine(start) == null)
					b = true;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		String str = null;
		String name = null;
		String s = null;
		initcodes = new Vector();
		Scanner init = null;
		while (scanner.hasNextLine()) 
		{
			str = scanner.nextLine();
			if (isSourceLine(str))
				break;
			init = null;
			init = new Scanner(str);
			if (init.hasNext())
			{
				s = init.next();
				if (isLineValid(s))
				{
					str = str.replace(',', ' ');
					init = new Scanner(str);
					code c = new code();
					str = init.next();
					c.adr = str;
					init.next();
					c.inst = init.next();
					if (init.hasNext())
						c.opr1 = init.next();
					if (init.hasNext())
						c.opr2 = init.next();
					if (init.hasNext())
						c.opr3 = init.next();
					initcodes.add(c);
				}
			}
		}
		fnames = new Vector();
		str = scanner.nextLine();
		vcodes = new Vector();
		vlines = new Vector();
		int line = 0;
		int cline = 0;
		for (; !str.equals(over); str = scanner.nextLine())
			if (isSourceLine(str))
			{
				code c = new code();
				c.adr = str;
				vcodes.add(c);
				vlines.add(Integer.valueOf(line));
				cline = line;
				line++;
			} else
			{
				code c = new code();
				str = str.replace(',', ' ');
				Scanner wordscan = new Scanner(str);
				c.adr = wordscan.next();
				if (isLineValid(c.adr))
				{
					name = wordscan.next();
					if (isValidFunName(name))
					{
						fnames.add(name);
						snames = snames + name.substring(1, name.length() - 1) + " ";
					}
					c.inst = wordscan.next();
					if (wordscan.hasNext())
						c.opr1 = wordscan.next();
					if (wordscan.hasNext())
						c.opr2 = wordscan.next();
					if (wordscan.hasNext())
						c.opr3 = wordscan.next();
					vcodes.add(c);
					vlines.add(Integer.valueOf(cline));
					line++;
				}
				wordscan.close();
			}

		bcodes = new boolean[vcodes.size()];
		int len = bcodes.length;
		for (int i = 0; i < len; i++)
			if (isSourceLine(((code)vcodes.get(i)).adr))
				bcodes[i] = false;
			else
				bcodes[i] = true;

		acodes = new code[vcodes.size()];
		acodes = (code[])vcodes.toArray(acodes);
		scanner.close();
	}

	public int LookupSymbolTable(int offset, String function)
	{
		int len = symbols.size();
		for (int index = 0; index < len; index++)
		{
			symbol s = (symbol)symbols.get(index);
			if (s.offset == offset && s.function.equals(function))
				return index;
		}

		return -1;
	}

	public void ReadBlocks(Vector vproc, CfgDataStruct opcfgarray[])
	{
		nump = vproc.size();
		int j = 0;
		int i = 0;
		int k = 0;
		procs = new procedure[nump];
		for (int index = 0; index < nump; index++)
		{
			procs[index] = new procedure();
			procs[index].pn = opcfgarray[j].procnum;
			procs[index].numb = ((Integer)vproc.get(index)).intValue();
			procs[index].name = (String)fnames.get(index);
			procs[index].blocks = new procedure.block[((Integer)vproc.get(index)).intValue()];
			int len = procs[index].blocks.length;
			for (i = 0; i < len; i++)
				procs[index].blocks[i] = procs[index]. new block();

			for (i = 0; i < len; i++)
			{
				procs[index].blocks[i].bloop = opcfgarray[i + j].bloop;
				procs[index].blocks[i].bn = opcfgarray[i + j].blocknum;
				procs[index].blocks[i].pn = opcfgarray[i + j].procnum;
				procs[index].blocks[i].begin = GetCodeIndex(opcfgarray[i + j].address);
				int arg = opcfgarray[i + j].returnblocka;
				if (arg != -1 && arg > opcfgarray[i + j].blocknum)
					procs[index].blocks[arg].prev.add(Integer.valueOf(i));
				if (arg != -1)
					procs[index].blocks[i].succ.add(Integer.valueOf(arg));
				arg = opcfgarray[i + j].returnblockb;
				if (arg != -1 && arg > opcfgarray[i + j].blocknum)
					procs[index].blocks[arg].prev.add(Integer.valueOf(i));
				if (arg != -1)
					procs[index].blocks[i].succ.add(Integer.valueOf(arg));
			}

			for (i = 0; i < len - 1; i++)
				procs[index].blocks[i].end = procs[index].blocks[i + 1].begin - 1;

			j += len;
			for (i = 0; i < len; i++)
			{
				procs[index].blocks[i].regsize = RegSize;
				procs[index].blocks[i].prevsize = procs[index].blocks[i].prev.size();
				procs[index].blocks[i].aprev = new int[procs[index].blocks[i].prev.size()];
				for (k = 0; k < procs[index].blocks[i].prevsize; k++)
					procs[index].blocks[i].aprev[k] = ((Integer)procs[index].blocks[i].prev.get(k)).intValue();

			}

		}

		for (i = 0; i < nump - 1; i++)
		{
			j = procs[i].numb;
			procs[i].blocks[j - 1].end = procs[i + 1].blocks[0].begin - 1;
		}

		j = procs[nump - 1].numb;
		procs[nump - 1].blocks[j - 1].end = acodes.length - 1;
	}

	public int GetCodeIndex(String adr)
	{
		int len = acodes.length;
		int i;
		for (i = 0; i < len; i++)
		{
			if (acodes[i].adr.equals(adr))
				break;
			if (bcodes[i] && acodes[i].adr.compareTo(adr) > 0)
				return i - 1;
		}

		return i;
	}

	public int GetFunIndex(String name)
	{
		int len = fnames.size();
		int i = 0;
		for (i = 0; i < len; i++)
			if (((String)fnames.get(i)).equals(name))
				return i;

		return -1;
	}

	public void print()
	{
		int len = procs.length;
		for (int i = 0; i < len; i++)
		{
			System.out.print("proc:" + i);
			int k = procs[i].blocks.length;
			for (int j = 0; j < k; j++)
			{
				System.out.print(" block:  " + j);
				if (procs[i].blocks[j].idepend)
					System.out.println(" input dependent!");
				else
					System.out.println(" undependent!");
			}

		}

	}

	public String[] getCFiles()
	{
		return cFiles;
	}

	public void setCFiles(String files[])
	{
		cFiles = files;
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
