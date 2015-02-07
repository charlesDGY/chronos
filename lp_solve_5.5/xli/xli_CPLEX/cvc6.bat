@echo off

set src=xli_CPLEX.c lp_CPLEX.c lp_rlpt1.c lp_wlpt1.c yacc_read1.c lp_Hash1.c ../../lp_utils.c
set c=cl

%c% -I.. -I../.. -I../../shared /LD /MD /O2 /Zp8 /Gz -D_WINDLL -D_USRDLL -DWIN32 -DYY_NEVER_INTERACTIVE %src% ../lp_XLI.def -o xli_CPLEX.dll

if exist *.obj del *.obj
