// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Utils.java

package src;


public class Utils
{

	public Utils()
	{
	}

	public static int GetInstIndex(String inst)
	{
		if (inst.equals("lb"))
			return 0;
		if (inst.equals("lbu"))
			return 1;
		if (inst.equals("lh"))
			return 2;
		if (inst.equals("lhu"))
			return 3;
		if (inst.equals("lw"))
			return 4;
		if (inst.equals("dlw"))
			return 5;
		if (inst.equals("l.s"))
			return 6;
		if (inst.equals("l.d"))
			return 7;
		if (inst.equals("lwl"))
			return 8;
		if (inst.equals("lwr"))
			return 9;
		if (inst.equals("add"))
			return 10;
		if (inst.equals("addi"))
			return 11;
		if (inst.equals("addu"))
			return 12;
		if (inst.equals("addiu"))
			return 13;
		if (inst.equals("sub"))
			return 14;
		if (inst.equals("subu"))
			return 15;
		if (inst.equals("add.s"))
			return 16;
		if (inst.equals("add.d"))
			return 17;
		if (inst.equals("sub.s"))
			return 18;
		if (inst.equals("sub.d"))
			return 19;
		if (inst.equals("sb"))
			return 20;
		if (inst.equals("sh"))
			return 21;
		if (inst.equals("sw"))
			return 22;
		if (inst.equals("dsw"))
			return 23;
		if (inst.equals("dsz"))
			return 24;
		if (inst.equals("s.s"))
			return 25;
		if (inst.equals("s.d"))
			return 26;
		if (inst.equals("swl"))
			return 27;
		if (inst.equals("swr"))
			return 28;
		if (inst.equals("and"))
			return 29;
		if (inst.equals("andi"))
			return 30;
		if (inst.equals("or"))
			return 31;
		if (inst.equals("ori"))
			return 32;
		if (inst.equals("xor"))
			return 33;
		if (inst.equals("xori"))
			return 34;
		if (inst.equals("nor"))
			return 35;
		if (inst.equals("sll"))
			return 36;
		if (inst.equals("sllv"))
			return 37;
		if (inst.equals("srl"))
			return 38;
		if (inst.equals("srlv"))
			return 39;
		if (inst.equals("sra"))
			return 40;
		if (inst.equals("srav"))
			return 41;
		if (inst.equals("slt"))
			return 42;
		if (inst.equals("slti"))
			return 43;
		if (inst.equals("sltu"))
			return 44;
		if (inst.equals("sltiu"))
			return 45;
		if (inst.equals("mult"))
			return 46;
		if (inst.equals("multu"))
			return 47;
		if (inst.equals("div"))
			return 48;
		if (inst.equals("divu"))
			return 49;
		if (inst.equals("mfhi"))
			return 50;
		if (inst.equals("mthi"))
			return 51;
		if (inst.equals("mflo"))
			return 52;
		if (inst.equals("mtlo"))
			return 53;
		if (inst.equals("mul.s"))
			return 54;
		if (inst.equals("mul.d"))
			return 55;
		if (inst.equals("div.s"))
			return 56;
		if (inst.equals("div.d"))
			return 57;
		if (inst.equals("abs.s"))
			return 58;
		if (inst.equals("abs.d"))
			return 59;
		if (inst.equals("mov.s"))
			return 60;
		if (inst.equals("mov.d"))
			return 61;
		if (inst.equals("neg.s"))
			return 62;
		if (inst.equals("neg.d"))
			return 63;
		if (inst.equals("cvt.s.d"))
			return 64;
		if (inst.equals("cvt.s.w"))
			return 65;
		if (inst.equals("cvt.d.s"))
			return 66;
		if (inst.equals("cvt.d.w"))
			return 67;
		if (inst.equals("cvt.w.s"))
			return 68;
		if (inst.equals("cvt.w.d"))
			return 69;
		if (inst.equals("c.eq.s"))
			return 70;
		if (inst.equals("c.eq.d"))
			return 71;
		if (inst.equals("c.lt.s"))
			return 72;
		if (inst.equals("c.lt.d"))
			return 73;
		if (inst.equals("c.le.s"))
			return 74;
		if (inst.equals("c.le.d"))
			return 75;
		if (inst.equals("sqrt.s"))
			return 76;
		if (inst.equals("sqrt.d"))
			return 77;
		if (inst.equals("mfc1"))
			return 78;
		if (inst.equals("mtc1"))
			return 79;
		if (inst.equals("beq"))
			return 80;
		if (inst.equals("bne"))
			return 81;
		if (inst.equals("blez"))
			return 82;
		if (inst.equals("bgtz"))
			return 83;
		if (inst.equals("bltz"))
			return 84;
		if (inst.equals("bgez"))
			return 85;
		if (inst.equals("bc1f"))
			return 86;
		if (inst.equals("bc1t"))
			return 87;
		if (inst.equals("lui"))
			return 88;
		if (inst.equals("jal"))
			return 100;
		return !inst.equals("jr") ? -1 : 200;
	}

	public static int GetRegIndexV1(String reg)
	{
		if (reg.equals("$zero[0]"))
			return 0;
		if (reg.equals("$at[1]"))
			return 1;
		if (reg.equals("$v0[2]"))
			return 2;
		if (reg.equals("$v1[3]"))
			return 3;
		if (reg.equals("$a0[4]"))
			return 4;
		if (reg.equals("$a1[5]"))
			return 5;
		if (reg.equals("$a2[6]"))
			return 6;
		if (reg.equals("$a3[7]"))
			return 7;
		if (reg.equals("$t0[8]"))
			return 8;
		if (reg.equals("$t1[9]"))
			return 9;
		if (reg.equals("$t2[10]"))
			return 10;
		if (reg.equals("$t3[11]"))
			return 11;
		if (reg.equals("$t4[12]"))
			return 12;
		if (reg.equals("$t5[13]"))
			return 13;
		if (reg.equals("$t6[14]"))
			return 14;
		if (reg.equals("$t7[15]"))
			return 15;
		if (reg.equals("$s0[16]"))
			return 16;
		if (reg.equals("$s1[17]"))
			return 17;
		if (reg.equals("$s2[18]"))
			return 18;
		if (reg.equals("$s3[19]"))
			return 19;
		if (reg.equals("$s4[20]"))
			return 20;
		if (reg.equals("$s5[21]"))
			return 21;
		if (reg.equals("$s6[22]"))
			return 22;
		if (reg.equals("$s7[23]"))
			return 23;
		if (reg.equals("$t8[24]"))
			return 24;
		if (reg.equals("$t9[25]"))
			return 25;
		if (reg.equals("$k0[26]"))
			return 26;
		if (reg.equals("$k1[27]"))
			return 27;
		if (reg.equals("$gp[28]"))
			return 28;
		if (reg.equals("$sp[29]"))
			return 29;
		if (reg.equals("$s8[30]"))
			return 30;
		if (reg.equals("$ra[31]"))
			return 31;
		if (reg.equals("$hi"))
			return 32;
		if (reg.equals("$lo"))
			return 33;
		if (reg.equals("$f0"))
			return 34;
		if (reg.equals("$f1"))
			return 35;
		if (reg.equals("$f2"))
			return 36;
		if (reg.equals("$f3"))
			return 37;
		if (reg.equals("$f4"))
			return 38;
		if (reg.equals("$f5"))
			return 39;
		if (reg.equals("$f6"))
			return 40;
		if (reg.equals("$f7"))
			return 41;
		if (reg.equals("$f8"))
			return 42;
		if (reg.equals("$f9"))
			return 43;
		if (reg.equals("$f10"))
			return 44;
		if (reg.equals("$f11"))
			return 45;
		if (reg.equals("$f12"))
			return 46;
		if (reg.equals("$f13"))
			return 47;
		if (reg.equals("$f14"))
			return 48;
		if (reg.equals("$f15"))
			return 49;
		if (reg.equals("$f16"))
			return 50;
		if (reg.equals("$f17"))
			return 51;
		if (reg.equals("$f18"))
			return 52;
		if (reg.equals("$f19"))
			return 53;
		if (reg.equals("$f20"))
			return 54;
		if (reg.equals("$f21"))
			return 55;
		if (reg.equals("$f22"))
			return 56;
		if (reg.equals("$f23"))
			return 57;
		if (reg.equals("$f24"))
			return 58;
		if (reg.equals("$f25"))
			return 59;
		if (reg.equals("$f26"))
			return 60;
		if (reg.equals("$f27"))
			return 61;
		if (reg.equals("$f28"))
			return 62;
		if (reg.equals("$f29"))
			return 63;
		if (reg.equals("$f30"))
			return 64;
		if (reg.equals("$f31"))
			return 65;
		return !reg.equals("$fcc") ? -1 : 66;
	}

	public static int GetRegIndex(String reg)
	{
		if (reg.equals("$0"))
			return 0;
		if (reg.equals("$1"))
			return 1;
		if (reg.equals("$2"))
			return 2;
		if (reg.equals("$3"))
			return 3;
		if (reg.equals("$4"))
			return 4;
		if (reg.equals("$5"))
			return 5;
		if (reg.equals("$6"))
			return 6;
		if (reg.equals("$7"))
			return 7;
		if (reg.equals("$8"))
			return 8;
		if (reg.equals("$9"))
			return 9;
		if (reg.equals("$10"))
			return 10;
		if (reg.equals("$11"))
			return 11;
		if (reg.equals("$12"))
			return 12;
		if (reg.equals("$13"))
			return 13;
		if (reg.equals("$14"))
			return 14;
		if (reg.equals("$15"))
			return 15;
		if (reg.equals("$16"))
			return 16;
		if (reg.equals("$17"))
			return 17;
		if (reg.equals("$18"))
			return 18;
		if (reg.equals("$19"))
			return 19;
		if (reg.equals("$20"))
			return 20;
		if (reg.equals("$21"))
			return 21;
		if (reg.equals("$22"))
			return 22;
		if (reg.equals("$23"))
			return 23;
		if (reg.equals("$24"))
			return 24;
		if (reg.equals("$25"))
			return 25;
		if (reg.equals("$26"))
			return 26;
		if (reg.equals("$27"))
			return 27;
		if (reg.equals("$28"))
			return 28;
		if (reg.equals("$29"))
			return 29;
		if (reg.equals("$30"))
			return 30;
		if (reg.equals("$31"))
			return 31;
		if (reg.equals("$hi"))
			return 32;
		if (reg.equals("$lo"))
			return 33;
		if (reg.equals("$f0"))
			return 34;
		if (reg.equals("$f1"))
			return 35;
		if (reg.equals("$f2"))
			return 36;
		if (reg.equals("$f3"))
			return 37;
		if (reg.equals("$f4"))
			return 38;
		if (reg.equals("$f5"))
			return 39;
		if (reg.equals("$f6"))
			return 40;
		if (reg.equals("$f7"))
			return 41;
		if (reg.equals("$f8"))
			return 42;
		if (reg.equals("$f9"))
			return 43;
		if (reg.equals("$f10"))
			return 44;
		if (reg.equals("$f11"))
			return 45;
		if (reg.equals("$f12"))
			return 46;
		if (reg.equals("$f13"))
			return 47;
		if (reg.equals("$f14"))
			return 48;
		if (reg.equals("$f15"))
			return 49;
		if (reg.equals("$f16"))
			return 50;
		if (reg.equals("$f17"))
			return 51;
		if (reg.equals("$f18"))
			return 52;
		if (reg.equals("$f19"))
			return 53;
		if (reg.equals("$f20"))
			return 54;
		if (reg.equals("$f21"))
			return 55;
		if (reg.equals("$f22"))
			return 56;
		if (reg.equals("$f23"))
			return 57;
		if (reg.equals("$f24"))
			return 58;
		if (reg.equals("$f25"))
			return 59;
		if (reg.equals("$f26"))
			return 60;
		if (reg.equals("$f27"))
			return 61;
		if (reg.equals("$f28"))
			return 62;
		if (reg.equals("$f29"))
			return 63;
		if (reg.equals("$f30"))
			return 64;
		if (reg.equals("$f31"))
			return 65;
		return !reg.equals("$fcc") ? -1 : 66;
	}
}
