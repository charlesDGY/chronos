@echo off

set src=commonlib.c myblas.c lusol.c lusolio.c hbio.c mmio.c lusolmain.c
set c=cl

%c% /O2 /Zp8 /Gd -DWIN32 %src% -o LUSOL.exe

if exist *.obj del *.obj
