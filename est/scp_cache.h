#ifndef __MPA_CACHE_H_
#define __MPA_CACHE_H_
#include "scp_address.h"

/*** SCOPED CACHE ANALYSIS ***/

#define L1_DCACHE_ANALYSIS 1
#define L2_DCACHE_ANALYSIS 2
#define UNIFIED_CACHE_ANALYSIS 3
//int current_analysis;
#define SBLK_FREE 3
#define SBLK_DONE 4
#define ACS_IDENTICAL 0
#define ACS_NOT_IDENTICAL 1
#define UNKNOWN_AGE	-1

int printflag;
#define EVICTED     (X+1)
#define MAX_A       4       /*MAX_CACHE_ASSOCIATIVITY*/
struct scope_block {
	saddr_p m; /*scope address of this scoped block*/
	mem_blk_set_t* inst_block;/* address of instruction memory block */
#if 0
	int age; /*relative age of this scoped block*/
	saddr_p ys[MAX_A]; /*younger set*/
#endif
	int flag;

	int scp_age; /*scp_age = |yes_set| + |inst_ys_set| + 1*/
	worklist_p ys_set; /* contains temporal-scopes*/
	worklist_p inst_ys_set;/* contains instruction memory blocks*/
};
typedef struct scope_block sblk_s;
typedef struct scope_block* sblk_p;

sblk_p createSBlk(saddr_p blkAddr);
void cpySBlk(sblk_p dst, sblk_p src);
int mergeSBlk(sblk_p dst, sblk_p src);
int cmpSBlk(sblk_p sblk1, sblk_p sblk2);

//add scoped address mAcc to younger set of scoped cache block acsBlk
int addToYS(sblk_p acsBlk, saddr_p mAcc);
int clearYS(sblk_p acsBlk);
int unionYS(sblk_p dstBlk, sblk_p srcBlk);

/*ACS structure: scp_acs[i]=cacheSet[i], addr in increasing order*/
#define scp_acs worklist_p* 

void addToIncSet(saddr_p m, worklist_p *prvNode, worklist_p *strNode,
		loop_t *lp);

/****** SCOPED PERSISTENCE ANALYSIS FUNCTION *******/
#define WRITE_THRU  1
#define AVOID_ONE_ACCESS_MULTIPLE_AGING 1
/*fdct not converge if AVOID_MULTIPLE_AGING*/
#define AGED        0x1
#if 0
/* scope-aware update PS, update acs after accessing addr_set in loop lp*/
int canRenew(dat_inst_t* d_inst, int lpId);
void PS_update(scp_acs acs_out, worklist_p addr_set, loop_t *lp);

/* scope-aware join PS, to join two ACS */
int PS_join(scp_acs src, scp_acs dst, loop_t *lp);

/* cache analysis within a basic block */
void transform_bbi_dcache(tcfg_node_t *bbi, loop_t* lp, int type);

/****** estimate cache miss of PS blks in loop L ******/

/* collect scoped blks persistent when entering loop lp */
void getOuterPS(loop_t* lp);

/* estimate cold miss of persistent blks each time entering lp*/
void estLpMissPS(loop_t* lp);

/***** estimate all miss of non-persistent blks in node bbi ******/
void estNodeAllMiss(tcfg_node_t* bbi);

/***** multi-level cache analysis framework *****/

/* PS analysis within loop lp */
void analyze_loop_ps(loop_t *lp);

/*general handler for mpa analysis framework*/
void mpa_datacache();
#endif
void categorize_ul2_inst_PS_NC(int bbi_id, scp_acs mpa_acs_in, int inst_id,
		de_inst_t* inst, loop_t*lp);
void scp_data_update(scp_acs acs, worklist_p addr_in, loop_t*lp);
char** inst_psnc_ul2;
loop_t*** inst_ps_loop_ul2;
int get_mblk_hitmiss_ul2(tcfg_node_t*, int, loop_t*, int);
int loop_dist(loop_t* lp1, loop_t* lp2);
int scp_addrINacs(saddr_p maddr, scp_acs acs,loop_t*lp);
int scp_cmp_saddr(saddr_p dst, saddr_p src, loop_t* lp);
sblk_p scp_join_search(saddr_p mblk, worklist_p cacheset, loop_t*lp);
sblk_p scp_join_search_inst(mem_blk_set_t* iblock, worklist_p cacheset);
int scp_compareACS(scp_acs acs_a, scp_acs acs_b, loop_t*lp);
int scp_addrINacs(saddr_p maddr, scp_acs acs, loop_t*lp);
loop_t* scp_inner_ps_loop(worklist_p addrset);
void scp_update_Sblk_age(sblk_p sblk);
void uni_update_inst(scp_acs acs, unsigned inst_block);
void scp_test_cache_miss();
void scp_compare_sorted_ACSs(loop_t*lp);

#endif
