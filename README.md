gui program execution analysis
====================================

click "open file"
----------------------------------

## call openDirectory() in MainFrame

> 1. check whether having set the directory of gcc,lp_solver and simplesim
> 2. set work dir and path
> 3. call shell.callrm()   //run rm.sh, rm *.cfg and loop.info
> 4. call shell.callCompile()  //run compile.sh, **question:in compile.sh Ln 27 $dis?, Ln 37 $est** 
     *answer: $dis calls a program to disassemble the compiled obj file and generates an obj.dis file to record the assemble code and information*

     *answer: $est calls cfg function to generate an obj.cfg file which record the block information and edge connection.*


