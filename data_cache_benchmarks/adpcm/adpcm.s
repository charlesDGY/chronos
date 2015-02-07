	.file	1 "adpcm.c"

 # GNU C 2.7.2.3 [AL 1.1, MM 40, tma 0.1] SimpleScalar running sstrix compiled by GNU C

 # Cc1 defaults:
 # -mgas -mgpOPT

 # Cc1 arguments (-G value = 8, Cpu = default, ISA = 1):
 # -quiet -dumpbase -o

gcc2_compiled.:
__gnu_compiled_c:
	.globl	indexTable
	.data
	.align	2
indexTable:
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	2
	.word	4
	.word	6
	.word	8
	.word	-1
	.word	-1
	.word	-1
	.word	-1
	.word	2
	.word	4
	.word	6
	.word	8
	.globl	stepsizeTable
	.align	2
stepsizeTable:
	.word	7
	.word	8
	.word	9
	.word	10
	.word	11
	.word	12
	.word	13
	.word	14
	.word	16
	.word	17
	.word	19
	.word	21
	.word	23
	.word	25
	.word	28
	.word	31
	.word	34
	.word	37
	.word	41
	.word	45
	.word	50
	.word	55
	.word	60
	.word	66
	.word	73
	.word	80
	.word	88
	.word	97
	.word	107
	.word	118
	.word	130
	.word	143
	.word	157
	.word	173
	.word	190
	.word	209
	.word	230
	.word	253
	.word	279
	.word	307
	.word	337
	.word	371
	.word	408
	.word	449
	.word	494
	.word	544
	.word	598
	.word	658
	.word	724
	.word	796
	.word	876
	.word	963
	.word	1060
	.word	1166
	.word	1282
	.word	1411
	.word	1552
	.word	1707
	.word	1878
	.word	2066
	.word	2272
	.word	2499
	.word	2749
	.word	3024
	.word	3327
	.word	3660
	.word	4026
	.word	4428
	.word	4871
	.word	5358
	.word	5894
	.word	6484
	.word	7132
	.word	7845
	.word	8630
	.word	9493
	.word	10442
	.word	11487
	.word	12635
	.word	13899
	.word	15289
	.word	16818
	.word	18500
	.word	20350
	.word	22385
	.word	24623
	.word	27086
	.word	29794
	.word	32767
	.text
	.align	2
	.globl	main

	.comm	pcmdata,4096

	.comm	adpcmdata,1024

	.comm	pcmdata_2,4096

	.comm	coder_1_state,4

	.comm	coder_2_state,4

	.comm	decoder_state,4

	.comm	state,4

	.text

	.loc	1 81
	.ent	main
main:
	.frame	$fp,120,$31		# vars= 96, regs= 2/0, args= 16, extra= 0
	.mask	0xc0000000,-4
	.fmask	0x00000000,0
	subu	$sp,$sp,120
	sw	$31,116($sp)
	sw	$fp,112($sp)
	move	$fp,$sp
	jal	__main
	sw	$0,36($fp)
	la	$2,adpcmdata
	sw	$2,52($fp)
	la	$2,pcmdata
	sw	$2,60($fp)
	li	$2,0x00000800		# 2048
	sw	$2,108($fp)
	lh	$2,state
	sw	$2,84($fp)
	lb	$2,state+2
	sw	$2,92($fp)
	li	$2,0x00000001		# 1
	sw	$2,104($fp)
$L2:
	lw	$2,108($fp)
	bgtz	$2,$L5
	j	$L3
$L5:
	lw	$2,60($fp)
	addu	$3,$2,2
	sw	$3,60($fp)
	lh	$2,0($2)
	sw	$2,64($fp)
	lw	$2,64($fp)
	lw	$3,84($fp)
	subu	$2,$2,$3
	sw	$2,68($fp)
	lw	$2,68($fp)
	bgez	$2,$L6
	li	$2,0x00000008		# 8
	j	$L7
$L6:
	move	$2,$0
$L7:
	sw	$2,72($fp)
	lw	$2,72($fp)
	beq	$2,$0,$L8
	lw	$2,68($fp)
	subu	$3,$0,$2
	sw	$3,68($fp)
$L8:
	sw	$0,76($fp)
	lw	$2,80($fp)
	sra	$3,$2,3
	sw	$3,88($fp)
	lw	$2,68($fp)
	lw	$3,80($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L9
	li	$2,0x00000004		# 4
	sw	$2,76($fp)
	lw	$2,68($fp)
	lw	$3,80($fp)
	subu	$2,$2,$3
	sw	$2,68($fp)
	lw	$2,88($fp)
	lw	$3,80($fp)
	addu	$2,$2,$3
	sw	$2,88($fp)
$L9:
	lw	$2,80($fp)
	sra	$3,$2,1
	sw	$3,80($fp)
	lw	$2,68($fp)
	lw	$3,80($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L10
	lw	$2,76($fp)
	ori	$3,$2,0x0002
	sw	$3,76($fp)
	lw	$2,68($fp)
	lw	$3,80($fp)
	subu	$2,$2,$3
	sw	$2,68($fp)
	lw	$2,88($fp)
	lw	$3,80($fp)
	addu	$2,$2,$3
	sw	$2,88($fp)
$L10:
	lw	$2,80($fp)
	sra	$3,$2,1
	sw	$3,80($fp)
	lw	$2,68($fp)
	lw	$3,80($fp)
	slt	$2,$2,$3
	bne	$2,$0,$L11
	lw	$2,76($fp)
	ori	$3,$2,0x0001
	sw	$3,76($fp)
	lw	$2,88($fp)
	lw	$3,80($fp)
	addu	$2,$2,$3
	sw	$2,88($fp)
$L11:
	lw	$2,72($fp)
	beq	$2,$0,$L12
	lw	$2,84($fp)
	lw	$3,88($fp)
	subu	$2,$2,$3
	sw	$2,84($fp)
	j	$L13
$L12:
	lw	$2,84($fp)
	lw	$3,88($fp)
	addu	$2,$2,$3
	sw	$2,84($fp)
$L13:
	lw	$2,84($fp)
	li	$3,0x00007fff		# 32767
	slt	$2,$3,$2
	beq	$2,$0,$L14
	li	$2,0x00007fff		# 32767
	sw	$2,84($fp)
	j	$L15
$L14:
	lw	$2,84($fp)
	slt	$3,$2,-32768
	beq	$3,$0,$L16
	li	$2,-32768			# 0xffff8000
	sw	$2,84($fp)
$L16:
$L15:
	lw	$2,76($fp)
	lw	$3,72($fp)
	or	$2,$2,$3
	sw	$2,76($fp)
	lw	$2,76($fp)
	move	$3,$2
	sll	$2,$3,2
	la	$3,indexTable
	addu	$2,$2,$3
	lw	$3,92($fp)
	lw	$2,0($2)
	addu	$3,$3,$2
	sw	$3,92($fp)
	lw	$2,92($fp)
	bgez	$2,$L17
	sw	$0,92($fp)
$L17:
	lw	$2,92($fp)
	slt	$3,$2,89
	bne	$3,$0,$L18
	li	$2,0x00000058		# 88
	sw	$2,92($fp)
$L18:
	lw	$2,92($fp)
	move	$3,$2
	sll	$2,$3,2
	la	$3,stepsizeTable
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,80($fp)
	lw	$2,104($fp)
	beq	$2,$0,$L19
	lw	$3,76($fp)
	sll	$2,$3,4
	andi	$3,$2,0x00f0
	sw	$3,100($fp)
	j	$L20
$L19:
	lw	$2,52($fp)
	addu	$3,$2,1
	sw	$3,52($fp)
	lbu	$3,76($fp)
	andi	$4,$3,0x000f
	move	$3,$4
	lbu	$4,100($fp)
	or	$3,$3,$4
	sb	$3,0($2)
$L20:
	lw	$2,104($fp)
	xori	$3,$2,0x0000
	sltu	$2,$3,1
	sw	$2,104($fp)
$L4:
	lw	$3,108($fp)
	subu	$2,$3,1
	move	$3,$2
	sw	$3,108($fp)
	j	$L2
$L3:
	lhu	$2,84($fp)
	sh	$2,state
	lbu	$2,92($fp)
	sb	$2,state+2
	lw	$2,104($fp)
	bne	$2,$0,$L21
	lw	$2,52($fp)
	addu	$3,$2,1
	sw	$3,52($fp)
	lbu	$3,100($fp)
	sb	$3,0($2)
$L21:
	la	$2,pcmdata_2
	sw	$2,56($fp)
	la	$2,adpcmdata
	sw	$2,48($fp)
	li	$2,0x00000800		# 2048
	sw	$2,108($fp)
	lh	$2,state
	sw	$2,84($fp)
	lb	$2,state+2
	sw	$2,92($fp)
	lw	$2,92($fp)
	move	$3,$2
	sll	$2,$3,2
	la	$3,stepsizeTable
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,80($fp)
	sw	$0,104($fp)
$L22:
	lw	$2,108($fp)
	bgtz	$2,$L25
	j	$L23
$L25:
	lw	$2,104($fp)
	beq	$2,$0,$L26
	lw	$2,96($fp)
	andi	$3,$2,0x000f
	sw	$3,76($fp)
	j	$L27
$L26:
	lw	$2,48($fp)
	addu	$3,$2,1
	sw	$3,48($fp)
	lb	$2,0($2)
	sw	$2,96($fp)
	lw	$3,96($fp)
	sra	$2,$3,4
	andi	$3,$2,0x000f
	sw	$3,76($fp)
$L27:
	lw	$2,104($fp)
	xori	$3,$2,0x0000
	sltu	$2,$3,1
	sw	$2,104($fp)
	lw	$2,76($fp)
	move	$3,$2
	sll	$2,$3,2
	la	$3,indexTable
	addu	$2,$2,$3
	lw	$3,92($fp)
	lw	$2,0($2)
	addu	$3,$3,$2
	sw	$3,92($fp)
	lw	$2,92($fp)
	bgez	$2,$L28
	sw	$0,92($fp)
$L28:
	lw	$2,92($fp)
	slt	$3,$2,89
	bne	$3,$0,$L29
	li	$2,0x00000058		# 88
	sw	$2,92($fp)
$L29:
	lw	$2,76($fp)
	andi	$3,$2,0x0008
	sw	$3,72($fp)
	lw	$2,76($fp)
	andi	$3,$2,0x0007
	sw	$3,76($fp)
	lw	$2,80($fp)
	sra	$3,$2,3
	sw	$3,88($fp)
	lw	$3,76($fp)
	andi	$2,$3,0x0004
	beq	$2,$0,$L30
	lw	$2,88($fp)
	lw	$3,80($fp)
	addu	$2,$2,$3
	sw	$2,88($fp)
$L30:
	lw	$3,76($fp)
	andi	$2,$3,0x0002
	beq	$2,$0,$L31
	lw	$3,80($fp)
	sra	$2,$3,1
	lw	$3,88($fp)
	addu	$2,$3,$2
	sw	$2,88($fp)
$L31:
	lw	$3,76($fp)
	andi	$2,$3,0x0001
	beq	$2,$0,$L32
	lw	$3,80($fp)
	sra	$2,$3,2
	lw	$3,88($fp)
	addu	$2,$3,$2
	sw	$2,88($fp)
$L32:
	lw	$2,72($fp)
	beq	$2,$0,$L33
	lw	$2,84($fp)
	lw	$3,88($fp)
	subu	$2,$2,$3
	sw	$2,84($fp)
	j	$L34
$L33:
	lw	$2,84($fp)
	lw	$3,88($fp)
	addu	$2,$2,$3
	sw	$2,84($fp)
$L34:
	lw	$2,84($fp)
	li	$3,0x00007fff		# 32767
	slt	$2,$3,$2
	beq	$2,$0,$L35
	li	$2,0x00007fff		# 32767
	sw	$2,84($fp)
	j	$L36
$L35:
	lw	$2,84($fp)
	slt	$3,$2,-32768
	beq	$3,$0,$L37
	li	$2,-32768			# 0xffff8000
	sw	$2,84($fp)
$L37:
$L36:
	lw	$2,92($fp)
	move	$3,$2
	sll	$2,$3,2
	la	$3,stepsizeTable
	addu	$2,$2,$3
	lw	$3,0($2)
	sw	$3,80($fp)
	lw	$2,56($fp)
	addu	$3,$2,2
	sw	$3,56($fp)
	lhu	$3,84($fp)
	sh	$3,0($2)
$L24:
	lw	$3,108($fp)
	subu	$2,$3,1
	move	$3,$2
	sw	$3,108($fp)
	j	$L22
$L23:
	lhu	$2,84($fp)
	sh	$2,state
	lbu	$2,92($fp)
	sb	$2,state+2
	li	$2,0x00000001		# 1
	j	$L1
$L1:
	move	$sp,$fp			# sp not trusted here
	lw	$31,116($sp)
	lw	$fp,112($sp)
	addu	$sp,$sp,120
	j	$31
	.end	main
