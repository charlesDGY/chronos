@echo off

set src=commonlib.c myblas.c lusol.c lusolio.c hbio.c mmio.c lusolmain.c
set c=gcc

%c% -O3 %src% -o LUSOL.exe
