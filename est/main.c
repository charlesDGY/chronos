/*******************************************************************************
 *
 * Chronos: A Timing Analyzer for Embedded Software
 * =============================================================================
 * http://www.comp.nus.edu.sg/~rpembed/chronos/
 *
 * Copyright (C) 2005 Xianfeng Li
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * $Id: main.c,v 1.3 2006/07/15 03:22:50 lixianfe Exp $
 *
 ******************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "cfg.h"
#include "bpred.h"
#include "cache.h"
#include "address.h"
#include "scp_cache.h"

char DEBUG_INFEAS = 0;

int PROGRESS_STEP = 10000;
extern int num_tcfg_edges;
extern int num_tcfg_nodes;
extern tcfg_node_t **tcfg;

extern char *run_opt;
extern FILE *filp, *fusr;

int bpred_scheme;
int enable_icache = 0;
int enable_scp_dcache = 0; /* Enable scope-aware dcache analysis */
int enable_scp_dl2cache = 0;/* Enable scope-aware level 2 dcache analysis */
//int enable_dcache = 0; /* For anabling dcache analysis */
int enable_il2cache = 0; /* For enabling level 2 icache analysis */
int enable_ul2cache = 0; /* For enabling level 2 ucache analysis */

int enable_abs_inst_cache = 0; /* For anabling abstract icache analysis */

int X, Y, B, l1, l2;
extern worklist_p*** scp_addrset_l1;
extern worklist_p*** scp_addrset_l2;

prog_t prog;

// vivy: infeasible path analysis
#include "infeasible.h"

// vivy: marker for procedures to include in estimation
char *include_proc;

/* sudiptac:: For performance measurement */
int findproc(int addr) {
	int i;
	for (i = 0; i < prog.num_procs; i++)
		if (prog.procs[i].sa == addr)
			return i;
	return -1;
}

// vivy: read list of functions to include in analysis
// Some analysis steps are expensive so avoid processing unnecessary functions.
int read_functions(char *obj_file) {
	FILE *fptr;
	char fname[80];

	int id;
	int addr;
	char name[80];

	include_proc = (char*) calloc(prog.num_procs, sizeof(char));

	sprintf(fname, "%s.fnlist", obj_file);
	fptr = fopen(fname, "r");

	if (!fptr) {
		if (DEBUG_INFEAS)
			printf(
					"%s.fnlist not found -- all procedures will be included in estimation.\n",
					obj_file);
		for (id = 0; id < prog.num_procs; id++)
			include_proc[id] = 1;
		return -1;
	}

	while (fscanf(fptr, "%x %s", &addr, name) != EOF) {
		id = findproc(addr);
		if (id != -1)
			include_proc[id] = 1;
		else
			printf("Warning: procedure [0x%x] %s not found.\n", addr, name);
	}
	fclose(fptr);
	return 0;
}
void my_dump_tcfg(int* unrolled_loop_map) {
	extern tcfg_node_t** tcfg;
	extern int num_tcfg_nodes;
	printf("\nnumber of tcfg node:%d\n", num_tcfg_nodes);
	int i;
	for (i = 0; i < num_tcfg_nodes; i++) {
		tcfg_node_t* bbi = tcfg[i];
		printf("tcfg node:%d (%d) lp:%d", i, bbi->id, loop_map[bbi->id]->id);
		printf("\tout: (");
		tcfg_edge_t* e;
		for (e = bbi->out; e != NULL; e = e->next_out) {
			printf(" %d", e->dst->id);
		}
		printf(")");
		printf("\tin: (");
		for (e = bbi->in; e != NULL; e = e->next_in) {
			printf(" %d", e->src->id);
		}
		printf(")\n");

	}
	printf("\n DUMP loop\n");
	for (i = 0; i < num_tcfg_loops; i++) {
		loop_t*lp = loops[i];
		printf("loop %d bound:%d", lp->id, lp->bound);
		if (lp->parent != NULL
		)
			printf(" parent:%d", lp->parent->id);
		if (unrolled_loop_map != NULL
		)
			printf(" mapping:%d", unrolled_loop_map[lp->id]);
		tcfg_node_t* head = lp->head;
		tcfg_node_t* tail = lp->tail;
		if (tail == NULL) {
			tail = tcfg[num_tcfg_nodes - 1];
		}
		printf("\n");
		printf("\thead: %d\n", head->id);
		printf("\ttail: %d\n", tail->id);
		printf("\n");
	}
//	printf("\nDUMP topo\n");
//	for (i = 0; i < num_tcfg_nodes; i++) {
//		tcfg_node_t* bbi = tcfg[topo_tcfg[i]];
//		printf("topo_id %d: %d\n", i, bbi->id);
//	}
#if 1

#endif
#if 0
	printf("checking instructions\n");
	if (enable_icache) {
		extern cache_t cache;
		for (i = 0; i < num_tcfg_nodes; i++) {
			tcfg_node_t* bbi = tcfg[i];
			printf("basic block %d\n", bbi->id);
			int inst_id;
			for (inst_id = 0; inst_id < bbi->bb->num_inst; inst_id++) {
				de_inst_t*inst = &(bbi->bb->code[inst_id]);
				int mblk = MBLK_ID(bbi->bb->sa,inst->addr);
				int offset = CACHE_LINE(inst->addr);
				printf("\tinst %d: 0x%x (%d) offset=%d\n", inst_id, inst->addr,
						mblk, offset);
			}
		}
	}
#endif
}
// program flow analysis to construct control flow graphs from objective code
static void path_analysis(char *fName) {
	char obj_file[256];
	// read object code, decode it
	strcpy(obj_file, fName);
	read_code(obj_file);

	// create procs and their CFGs from the decoded text
	build_cfgs();

	// vivy: read list of functions to include in estimation
	// do this after prog.procs are established
	strcpy(obj_file, fName);
	read_functions(obj_file);

	// transform the CFGs into a global CFG called tcfg (transformed-cfg);
	prog_tran();

	// identify loop levels as well as block-loop mapping
	loop_process();
#if 0
	int i;
	for (i=0;i<num_tcfg_loops;i++) {
		loop_t* lp = loops[i];
		tcfg_node_t* lpHead= lp->head;
		printf("\nloop %d  lpHead bb:%d\n",lp->id,lpHead->bb->id);
		if (lp->parent!=NULL) printf("\tparent:%d\n",lp->parent->id);
	}
	exit(0);
#endif

#if 0
	// find topological order of all tcfg nodes
	// topo_tcfg[topo_id] = tcfg_id
	set_topological_tcfg();
#endif
	/********************************************/
	/* vivy: infeasible path analysis */
#if 0
	my_dump_tcfg(NULL);
#endif
	strcpy(obj_file, fName);
	infeas_analysis(obj_file);
#if 0
	exit(0);
#endif
	/********************************************/
	if (enable_scp_dcache) {
		extern int assoc_dl1, nsets_dl1, bsize_dl1, mem_lat[2];
		X = assoc_dl1, Y = nsets_dl1, B = bsize_dl1, l1 = mem_lat[0], l2 = 0;
		scp_pre_address_analysis(fName, &scp_addrset_l1);
		if (enable_scp_dl2cache || enable_ul2cache) {
			extern int assoc_dl2, nsets_dl2, bsize_dl2, cache_dl2_lat,
					cache_dl1_lat;
			X = assoc_dl2, Y = nsets_dl2, B = bsize_dl2, l1 = cache_dl2_lat, l2 =
					mem_lat[0];

			scp_pre_address_analysis(fName, &scp_addrset_l2);
		}
		/*
		 * TODO: perform address analysis here, store the addresses in ...
		 */
		scp_store_address_set();
	} else {
		/*
		 * set loop-bound for loops
		 * NOTE: If scp_enable_dcache is enabled ,
		 * the loop bound is set during address analysis
		 */
		int i;
		for (i = 0; i < prog.num_procs; i++) {
			inf_proc_t* ip = &(inf_procs[i]);
			int j;
			for (j = 0; j < ip->num_bb; j++) {
				inf_node_t* ib = &(ip->inf_cfg[j]);
				loop_t*lp = (loop_t*) getIbLoop(ib);
				if (lp) {
					lp->bound = getIbLB(ib);
					lp->rId = lp->rType = lp->rBound = -1;
				}
			}
		}
		loops[0]->bound = 1;
	}
	/********************************************/

	if (enable_icache) {
		/*
		 * TODO: store loop_id, loop_bound
		 */
		int old_num_tcfg_loops = num_tcfg_loops;
		int* old_loop_id = calloc(old_num_tcfg_loops, sizeof(int));
		int* old_loop_bound = calloc(old_num_tcfg_loops, sizeof(int));
		int i;
		for (i = 0; i < num_tcfg_loops; i++) {
			old_loop_id[i] = loops[i]->id;
			old_loop_bound[i] = loops[i]->bound;
		}
		/******************************************/
		virtual_unroll();
		/* recollect the tcfg edges as the transformed CFG has been
		 * augmented after virtual unrolling */
		collect_tcfg_edges();
		build_bbi_map();
		loop_process();
		/*
		 *  find topological order of all tcfg nodes
		 *  topo_tcfg[topo_id] = tcfg_id
		 */
		set_topological_tcfg();
		/*****************************************/
		/*
		 * TODO: calculate loop_bound
		 */
		for (i = 0; i < num_tcfg_loops; i++) {
			loop_t* lp = loops[i];
			tcfg_node_t* head = lp->head;
			cfg_node_t* head_bb = head->bb;
			inf_proc_t* iproc = &(inf_procs[head_bb->proc->id]);
			inf_node_t* ihead = &(iproc->inf_cfg[head_bb->id]);
			lp->bound = getIbLB(ihead) - 1;
		}
		loops[0]->bound = 1; //TODO: test here
		/*****************************************/
		if (enable_scp_dcache) {
			//TODO: recalculate temporal scopes
			/*
			 * TODO: create a mapping from new loops to old loops
			 */
			int* unrolled_loop_map = calloc(num_tcfg_loops, sizeof(int));
			for (i = 0; i < num_tcfg_loops; i++) {
				tcfg_node_t* head = loops[i]->head;
				cfg_node_t* head_bb = head->bb;
				inf_proc_t* iproc = &(inf_procs[head_bb->proc->id]);
				inf_node_t* ihead = &(iproc->inf_cfg[head_bb->id]);
				if (ihead->loop_id == -1) {
					unrolled_loop_map[loops[i]->id] = 0;
				} else {
					unrolled_loop_map[loops[i]->id] =
							old_loop_id[inf_loops[ihead->loop_id].loop_id];
				}
			}
			/*
			 * TODO: filter the temporal scope
			 */
			scp_recalculate_temporal_scope(unrolled_loop_map, old_loop_bound);
		}
	}
	/**********************************************/
#if 0
	/*
	 * NOTE: checking the consistency between loop id(s) of temporal scope(s)
	 * and the loops of basic block
	 * If there are any problems, program is terminated with error flag.
	 */
	my_dump_tcfg(NULL);
//	scp_dump_address();
	int i;
	/*for (i=0;i<num_tcfg_nodes;i++) {
	 tcfg_node_t* bbi = tcfg[i];
	 printf("Basic block %d\n",bbi->id);
	 int j;
	 for (j= 0; j< bbi->bb->num_inst;j++) {
	 de_inst_t* inst = &(bbi->bb->code[j]);
	 printf("\tinst[%d]: 0x%x",j,inst->addr);
	 if (inst_type(inst) == INST_LOAD )
	 printf("[LOAD]");
	 else if (inst_type(inst) == INST_STORE)
	 printf("[STORE]");
	 printf("\n");
	 }
	 }*/
	for (i = 0; i < num_tcfg_nodes; i++) {
		tcfg_node_t* bbi = tcfg[i];
		if (bbi->bb->num_d_inst == 0)
		continue;
		printf("Basic block:%d bb:%d(%d)\n", bbi->id,bbi->bb->id,bbi->bb->num_d_inst);
		loop_t* innerLoop = loop_map[bbi->id];
		worklist_p innerList = NULL;
		worklist_p outerList = NULL;
		loop_t* lpPointer = innerLoop;
		for (; lpPointer != NULL; lpPointer = lpPointer->parent) {
			addToWorkList(&outerList, lpPointer);
		}
		worklist_p lNode;
		for (lNode = outerList; lNode != NULL; lNode = lNode->next) {
			addToWorkList(&innerList, lNode->data);
		}
		int j;
		int bugs=0;
		for (j = 0; j < bbi->bb->num_d_inst; j++) {
			dat_inst_t* dat_inst = bbi->bb->d_instlist;
			dat_inst = dat_inst + j;
			insn_t* insn = dat_inst->insn;
			worklist_p addrnode = bbi->addrset_l1[j];
			for (; addrnode != NULL; addrnode = addrnode->next) {
				saddr_p saddr = addrnode->data;
				worklist_p tsNode = saddr->tsList;
				for (;tsNode!=NULL;tsNode=tsNode->next) {
					ts_p ts = tsNode->data;
					loop_t* lpNode = innerLoop;
					int flag =0;
					for (;lpNode!=NULL;lpNode=lpNode->parent) {
						if (lpNode->id == ts->loop_id) {
							flag=1;
							break;
						}
					}
					if (!flag) {
						bugs=1;
						printf("\tdinst[%d] 0x%s: 0x%x ",j,insn->addr,saddr->blkAddr);
						scp_print_ts(saddr->tsList);
						printf("\t|");
						for (lNode = innerList;lNode!=NULL;lNode = lNode->next) {
							loop_t* tmp = lNode->data;
							printf(" l%d",tmp->id);
						}
						printf("\n");
					}
				}
			}
		}
		if (bugs) {
			printf("inconsistency BUGGG\n");
			exit(1);
		}
	}

	//int i;
	int * visited = calloc(num_tcfg_nodes, sizeof(int));
	worklist_p qHead, qTail;
	for (i = 0; i < num_tcfg_loops; i++) {
		loop_t* lup = loops[i];
		tcfg_node_t* lpHead = lup->head;
		int j;
		for (j = 0; j < num_tcfg_nodes; j++) {
			visited[j] = 0;
		}
		qHead = qTail = NULL;
		scp_enqueue(lpHead, &qHead, &qTail);
		visited[lpHead->id] = 1;
		printf("loop level %d\n",lup->id);
		while (scp_empty_queue(qHead, qTail) == 0) {
			tcfg_node_t* curNode = scp_dequeue(&qHead, &qTail);
			printf("\tbbi:%d\n",curNode->id);
			tcfg_edge_t* out_e;
			for (out_e = curNode->out; out_e != NULL; out_e = out_e->next_out) {
				if (scp_cmpLpOrder(lup->id, loop_map[out_e->dst->id]->id) >= 0) { //TODO: change here
					if (visited[out_e->dst->id] == 0) {
						scp_enqueue(out_e->dst, &qHead, &qTail);
						visited[out_e->dst->id] = 1;
					}
				}
			}
		}
	}
	//exit(0);
#endif
}

static void microarch_modeling() {
	if (bpred_scheme != NO_BPRED)
		bpred_analysis();

	pipe_analysis();
}

static void do_ilp(char *obj_file) {
	int exit_val;
	char s[256];

	//printf("do_ilp...\n");
	sprintf(s, "%s.lp", obj_file);
	filp = fopen(s, "w");
	sprintf(s, "%s.cons", obj_file);
	fusr = fopen(s, "r");
	if ((filp == NULL) || (fusr == NULL)) {
		fprintf(stderr, "fail to open ILP/CONS files for writing/reading\n");
		exit(1);
	}
	constraints();

	fclose(filp);
	fclose(fusr);

	/* vivy: print a cplex version */
	sprintf(s, "%s.ilp", obj_file);
	filp = fopen(s, "w");
	fprintf(filp, "enter Q\n");
	fclose(filp);

	// same with lp_solve format but no comment supported
	// sprintf( s, "sed '/\\\\/d' %s.lp >> %s.ilp", obj_file, obj_file );
	// EDIT: cplex supports the same comment format as lp_solve
	sprintf(s, "cat %s.lp >> %s.ilp", obj_file, obj_file);
	system(s);

	sprintf(s, "%s.ilp", obj_file);
	filp = fopen(s, "a");
	fprintf(filp, "optimize\n");
	fprintf(filp, "set logfile %s.sol\n", obj_file);
	fprintf(filp, "display solution objective\n");
	fprintf(filp, "display solution variables -\n");
	fprintf(filp, "quit\n");
	fclose(filp);

	/* Command:
	 * rm -f %s.sol; cplex < %s.ilp >/dev/null 2>/dev/null; cat %s.sol | sed '/^/s/Obj/obj/'
	 */
}

static void run_est(char *obj_file) {
	microarch_modeling();
	do_ilp(obj_file);
}

static void run_cfg(char *obj_file) {
	int i;
	char s[128];
	FILE *fcfg;

	sprintf(s, "%s.cfg", obj_file);
	//printf("dumping control flow graphs to file:%s\n", s);
	fcfg = fopen(s, "w");
	if (fcfg == NULL) {
		fprintf(stderr, "fail to create file: %s.cfg\n", s);
		exit(1);
	}

	for (i = 0; i < prog.num_procs; i++) {
		dump_cfg(fcfg, &prog.procs[i]);
	}
	fclose(fcfg);

	//printf("done.\n");
}

extern int fetch_width;

/* modification to indirect jump */
/* liangyun */

typedef struct jptb {
	addr_t adr;
	int ntarget;
	addr_t *ptarget;
	int index;
} jptb;

jptb * pjptb;
int bjptb = 0;
int njp;

int lookup_jptable(addr_t adr) {
	int i;
	int num = 0;
	for (i = 0; i < njp; i++) {
		if (pjptb[i].adr == adr) {
			num = pjptb[i].ntarget;
		}
	}
	return num;
}

void get_jptable(addr_t src, int index, addr_t * target) {
	int i;
	for (i = 0; i < njp; i++) {
		if (pjptb[i].adr == src) {
			*target = pjptb[i].ptarget[index];
			break;
		}
	}
	assert(i < njp);
}

int get_jptable_static(addr_t src, addr_t * target) {
	int i, j;
	int tail = -1;
	for (i = 0; i < njp; i++) {
		if (pjptb[i].adr == src) {
			j = pjptb[i].index;
			*target = pjptb[i].ptarget[j];
			pjptb[i].index++;
			if (pjptb[i].index == pjptb[i].ntarget)
				tail = 0;
			else
				tail = 1;
			break;
		}
	}
	//assert(i < njp);
	return tail;
}

void read_injp(char * objfile) {
	int tablesize, i, j;
	char file[100];
	FILE *ftable;
	sprintf(file, "%s.jtable", objfile);
	ftable = fopen(file, "r");
	if (!ftable) {
		// no indirect jump
		bjptb = 0;
	} else {
		bjptb = 1;
		fscanf(ftable, "%d", &tablesize);
		njp = tablesize;
		pjptb = (jptb *) calloc(tablesize, sizeof(jptb));
		for (i = 0; i < tablesize; i++) {
			fscanf(ftable, "%08x", &pjptb[i].adr);
			fscanf(ftable, "%d", &pjptb[i].ntarget);
			pjptb[i].index = 0;
			pjptb[i].ptarget = (addr_t *) calloc(pjptb[i].ntarget,
					sizeof(addr_t));
			for (j = 0; j < pjptb[i].ntarget; j++)
				fscanf(ftable, "%08x", &pjptb[i].ptarget[j]);
		}
		fclose(ftable);
	}
	//printf("bool: %d\n",bjptb);
}

int *pdepth;
int bdepth = 0;

int test_depth(int pid, int depth) {
	if (depth < pdepth[pid])
		return 1;
	else
		return 0;
}

void read_recursive(char * objfile) {
	char file[100];
	FILE *ftable;
	int size, i;
	sprintf(file, "%s.recursive", objfile);
	ftable = fopen(file, "r");
	if (!ftable) {
		bdepth = 0;
	} else {
		bdepth = 1;
		fscanf(ftable, "%d", &size);
		pdepth = (int *) calloc(size, sizeof(pdepth));
		for (i = 0; i < size; i++)
			fscanf(ftable, "%d", &pdepth[i]);
		fclose(ftable);
	}
}
#if 0
/* HBK: scope-aware data cache analysis */
int X,Y,B,l1,l2;
extern int assoc_dl1, nsets_dl1,bsize_dl1,mem_lat[2];
static void scp_aware_datacache_analysis(char *bin_fname) {
	//printf("\nADDRESS ANALYSIS: %s\n", bin_fname);
	ticks a, b;
	//X = 2; Y = 32; B = 32; l1 = 6; l2 = 0;//no L2 cache
	//X = assoc_dl1; Y = nsets_dl1;B = bsize_dl1;l1=mem_lat[0];
	/*
	 * TODO: unnecessary to perform address analysis, just copy
	 * approriate temporal scope from scp_addrset_l1,scp_addreset_l2
	 */
	/*
	 a = getticks();
	 //classified_address_analysis(bin_fname);
	 b = getticks();
	 printf("\n===================================================\n");
	 printf("Address analysis time = %lf secs\n", (b - a) / ((1.0) * CPU_MHZ));
	 printf("===================================================\n");
	 */
	a = getticks();

	//NOTE: need to repair Vivy's AB BB infeasible detection
	enable_dcache = 0;
	printf("\nCACHE ANALYSIS: %s\n", bin_fname);
	fflush(stdout);
	//mpa_datacache();
	mpaex_datacache();
	b = getticks();
	printf("\n===================================================\n");
	printf("Cache analysis time = %lf secs\n", (b - a) / ((1.0) * CPU_MHZ));
	printf("===================================================\n");
}
#endif
/* sudiptac :::: analyze two level cache hierarchies */
static void analyze_cache_hierarchy() {
	inst_chmc_l1 = inst_chmc_l2 = NULL;
	inst_age_l1 = inst_age_l2 = NULL;
	printf("\n=========== Instruction cache analysis ===========\n\n");
	/**********************************************/
	ticks a, b;

	a = getticks();
#ifdef _DEBUG_CRPD
	printf("Starting cache analysis........\n");
#endif

	if (enable_icache) {
		/* FIXME: this flag need to be removed in final version */
		//enable_abs_inst_cache = 1;
		/* cleekee: duplicated here from run_est() to access mp instructions */
		if (bpred_scheme != NO_BPRED)
			collect_mp_insts();

		/* sudiptac : analyze instruction cache (abstract interpretation approach) */
		analyze_abs_instr_cache_all();
	}

	b = getticks();
#ifdef _DEBUG_CRPD
	printf("Finished cache analysis........\n");
#endif

	printf("===================================================\n");
	printf("Maximum cache analysis time = %lf msecs\n",
			1000 * (b - a) / ((1.0) * CPU_MHZ));
	printf("===================================================\n");
}

static void scp_aware_analyze_cache_hierarchy(char* bin_fname) {
	if (enable_scp_dcache == 1) {
		extern int assoc_dl1, nsets_dl1, bsize_dl1, mem_lat[2];
		printf("\n\n================= L1 data cache ===================\n");
		/*initACS();*/
		X = assoc_dl1, Y = nsets_dl1, B = bsize_dl1, l1 = mem_lat[0], l2 = 0;
		mpaex_datacache(L1_DCACHE_ANALYSIS);
		if (enable_scp_dl2cache == 1 || enable_ul2cache == 1) {
			extern int assoc_dl2, nsets_dl2, bsize_dl2, cache_dl2_lat;
			if (enable_scp_dl2cache)
				printf(
						"\n\n================= L2 data cache ===================\n");
			else {
				printf(
						"\n\n=================== UL2 cache ======================\n");
			}
			X = assoc_dl2, Y = nsets_dl2, B = bsize_dl2, l1 = cache_dl2_lat, l2 =
					mem_lat[0];
			if (enable_scp_dl2cache == 1)
				mpaex_datacache(L2_DCACHE_ANALYSIS);
			else if (enable_ul2cache == 1) {
				set_cache_l2();
				mpaex_datacache(UNIFIED_CACHE_ANALYSIS);
			}
		}
		//	scp_dump_address();
	}
}

int main(int argc, char **argv) {
	int dbg = 1;
	char fName[256], str[256];
	//fName = calloc(256,sizeof(char));str = calloc(256,sizeof(char));
	strcpy(fName, argv[argc - 1]);
	if (dbg) {
		printf("\nFile name %s", fName);
		fflush(stdout);
		printf(
				"\n***NOTICE: you need to manually inline all procedures for address analysis to work");
		printf(
				"\n***NOTICE: for triangular loop, you need to create file <binary file name>.econ and set extra loop conditions to help Chronos recognize them");
		printf("\n Contrainst format: <type> L1_id L2_id k");
		printf("\n type = \"eql\" : L1's loop bound <= L2's iteration + k");
		printf("\n type = \"inv\" : L1's loop bound <= k - L2's iteration");
	}
	if (argc <= 1) {
		fprintf(stderr, "Usage:\n");
		fprintf(stderr, "%s <options> <benchmark>\n", argv[0]);
		exit(1);
	}

	init_isa();

	/* read options including (1) actions; (2) processor configuration */
	read_opt(argc, argv);
#if 0
	printf("\n");
	if (enable_icache) {
		extern int nsets, assoc, bsize;
		extern int nsets_l2, assoc_l2, bsize_l2;
		extern int nsets_dl2, assoc_dl2, bsize_dl2;
		printf("l1 instruction cache\n");
		printf("\tnset:%d assoc:%d bsize:%d\n", nsets, assoc, bsize);
		if (enable_il2cache) {
			printf("l2 instruction cache\n");
			printf("\tnset_l2:%d assoc_l2:%d bsize_l2:%d\n", nsets_l2, assoc_l2,
					bsize_l2);
		} else if (enable_ul2cache) {
			printf("ul2 cache\n");
			extern cache_t cache_l2;
			printf("\tnset_dl2:%d assoc_dl2:%d bsize_dl2:%d\n", nsets_dl2,
					assoc_dl2, bsize_dl2);
			printf("\tnset_dl2:%d assoc_dl2:%d bsize_dl2:%d\n", cache_l2.ns,
					cache_l2.na, cache_l2.ls);

		}
	}
	if (enable_scp_dcache) {
		extern int nsets_dl1, assoc_dl1, bsize_dl1;
		extern int nsets_dl2, assoc_dl2, bsize_dl2;
		printf("l1 data cache\n");
		printf("\tnset_dl1:%d assoc_dl1:%d bsize_dl1:%d\n", nsets_dl1,
				assoc_dl1, bsize_dl1);
		if (enable_scp_dl2cache) {
			printf("l2 data cache\n");
			printf("\tnset_dl2:%d assoc_dl2:%d bsize_dl2:%d\n", nsets_dl2,
					assoc_dl2, bsize_dl2);

		}
	}
	exit(0);
#endif
	/* liangyun: read jump table if necessary */
	strcpy(str, fName);
	read_injp(str);

	/* liangyun: read depth table for recursive function */
	strcpy(str, fName);
	read_recursive(str);

	/* vivy: only these steps are needed to build CFG */
	if (strcmp(run_opt, "CFG") == 0) {
		strcpy(str, fName);
		read_code(str);
		build_cfgs();
		run_cfg(str);
		return 0;
	}

	strcpy(str, fName);
	path_analysis(str);

	strcpy(str, fName);
	analyze_cache_hierarchy();
	//scope-aware PS analysis for dcache
	scp_aware_analyze_cache_hierarchy(fName);
#if 0
	//scp_dump_instruction();
	scp_dump_address();
	//my_dump_tcfg(NULL);
//	exit(0);
#endif
	run_est(argv[argc - 1]);
#if 0
	/*
	 * NOTE: dump out WCET of each basic block in a specified context
	 */
	extern int*** extend_cpred_times;
	extern int* numberofContext;
	extern tcfg_edge_t **tcfg_edges;
	int i;
	for (i = 0; i < num_tcfg_edges; i++) {
		tcfg_edge_t* e = tcfg_edges[i];
		tcfg_node_t* src = e->src;
		tcfg_node_t* dst = e->dst;
		int num_context = numberofContext[src->id];
		int flag = (num_context > 1);
		int vContext;

		for (vContext = 0; vContext < num_context; vContext++) {
			printf("WCET of edge %d_%d in context %d: %d", src->id, dst->id,
					vContext, extend_cpred_times[i][vContext][0]);
			if (dst->bb->num_d_inst>0) {
				printf(" (%d)",dst->bb->num_d_inst);
			}
			printf("\n");
		}
	}
#endif
	return 0;
}
