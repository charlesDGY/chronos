Introduction
------------
What is lp_solve and what is it not?
The simple answer is, lp_solve is a Mixed Integer Linear Programming (MILP) solver.

It is a free (see LGPL for the GNU lesser general public license) linear (integer) programming solver
based on the revised simplex method and the Branch-and-bound method for the integers.
It contains full source, examples and manuals.
lp_solve solves pure linear, (mixed) integer/binary, semi-continuous and
special ordered sets (SOS) models.

See the reference guide for more information.


lp_solve 5.5
------------

Why a jump from version numbers 5.1 to 5.5 ?
This is done to indicate that this is more than just another update.
The solver engine was revised and optimised in such a way that performance has improved considerably.
Numerical stability is also better resulting in more models that can be solved.
The LUSOL bfp is now also the default. In the past, the etaPFI bfp package was the default,
but for larger models this leads faster to numerical instabilities and performance problems.

Overall, the v5.5 code is faster and more robust than v5.1.
This robustness is for example proven by the fact that many more models can now be solved even without scaling.

The API hasn't changed very much.
There are a couple of new routines and one routine has an extra argument.
Some constants got new values.

    * Fundamental internal change to the solver engine resulting in better performance and numerical stability.
      Both the LP solver and the B&B solvers are enhanced.
    * Optimised MILP branch truncation, with reduced cost fixing.
    * LUSOL bfp is now the default.
    * Presolve is improved in functionality and performance.
    * Better handling of degeneracy, with more options.
    * Store and read options from a file make it easier to set options.
    * Partial pricing for the primal simplex now works.
    * Full support for xli_ZIMPL v2.0.3.
    * The objective function is no longer stored as part of the constraint matrix.
    * Dual-long step code is in place, but not fully activated yet.
    * General code cleanup.
    * Added OBJSENSE and OBJNAME headers in the free MPS format (See MPS file format).
    * The MathProg xli driver has now the ability to generate a model.
    * New API routines

Start by taking a look at 'Changes compared to version 4', 'Changes from version 5.1 to version 5.5'
and 'lp_solve usage'
This gives a good starting point.


BFP's
-----

BFP stands for Basis Factorization Package, which is a unique lp_solve feature.  Considerable
effort has been put in this new feature and we have big expectations for this. BFP is a generic
interface model and users can develop their own implementations based on the provided templates.
We are very interested in providing as many different BFPs as possible to the community.

lp_solve 5.5 has the LUSOL BFP built in as default engine.  In addition two other
BFPs are included for both Windows and Linux: bfp_etaPFI.dll, bfp_GLPK.dll for Windows and
libbfp_etaPFI.so, libbfp_GLPK.so for Linux.  The bfp_etaPFI includes
advanced column ordering using the COLAMD library, as well as better pivot management for
stability.  For complex models, however, the LU factorization approach is much better, and
lp_solve now includes LUSOL as one of the most stable engines available anywhere.  LUSOL was
originally developed by Prof. Saunders at Stanford, and it has now been ported to C
and enhanced by Kjell.

If you compile BFPs yourself, make sure that under Windows, you use __stdcall convention and
use 8 byte alignments.  This is needed for the BFPs to work correctly with the general
distribution of lp_solve and also to make sharing BFPs as uncomplicated as possible.

See the reference guide for more information.


XLI's
-----

XLI stands for eXternal Language Interface, also a unique lp_solve feature. XLI's are stand-alone
libraries used as add-on to lp_solve to make it possible to read and write lp models in a format
not natively supported by lp_solve. Examples are CPLEX lp format, LINDO lp format, MathProg format,
XML format...

See the reference guide for more information.


lpsolve API
-----------

Don't forget that the API has changed compared to previous versions of lpsolve and that you just
can't use the version 5 lpsolve library with your version 4 or older code.  That is also the
reason why the library is now called lpsolve55.dll/lpsolve55.a.  lpsolve55.dll or lpsolve55.a are
only needed when you call the lpsolve library via the API interface from your program.
The lp_solve program is still a stand-alone executable program.

There are examples interfaces for different language like C, VB, C#, VB.NET, Java,
Delphi, and there is now also even a COM object to access the lpsolve library.  This means that
the door is wide-open for using lp_solve in many different situations.  Thus everything that is
available in version 4 is now also available in version 5 and already much more!

See the reference guide for more information.


Conversion between lp modeling formats
--------------------------------------

Note that lp2mps and mps2lp don't exist anymore. However this functionality is now implemented
in lp_solve:

lp2mps can be simulated as following:
lp_solve -parse_only -lp infile -wmps outfile

mps2lp can be simulated as following:
lp_solve -parse_only -mps infile -wlp outfile


via the -rxli option, a model can be read via an XLI library and via the -wxli option, a model
can be written via an XLI library.


How to build the executables yourself.
---------------------------------------

At this time, there are no Makefiles yet. However for the time being, there are batch files/scripts
to build. For the Microsoft compiler under Windows, use cvc6.bat, for the gnu compiler under Windows,
use cgcc.bat and for Unix/Linux, use the ccc shell script (sh ccc).

See the reference guide for more information.


IDE
---

Under Windows, there is now also a very user friendly lpsolve IDE. Check out LPSolveIDE

See the reference guide for more information.


Documentation (reference guide)
-------------------------------

See lp_solve55.chm for a Windows HTML help documentation file.
The html files are also in lp_solve_5.5_doc.tar.gz. Start with index.htm
Also see http://geocities.com/lpsolve/ for a on-line documentation


Change history:
---------------

17/05/05 version 5.5.0.0
- Beta release of version 5.5

??/??/05 version 5.5.0.1
- ?

26/06/05 version 5.5.0.2
- ?

29/06/05 version 5.5.0.3
- ?

16/08/05 version 5.5.0.4
- There are no API changes
- The LUSOL message routine could generate a crash under some cicumstances. Fixed
- A crash could occur when building the model in add_row_mode. Fixed.
- write_params didn't write the PRESOLVE and PRESOLVELOOPS correctly. Fixed.
- write_params didn't write constants with value 0. Fixed.
- The library did not compile under msdev 2002 (VC 7.0 _MSC_VER 1300). Fixed.
- There were some problems with printing long long variables which could generate a crash. Fixed.
- An overflow error could occur because memory was sometimes overwritten. Fixed.
- Presolve routines are revised. They are again improved and made faster.
  Also some problems with it are fixed (possible crashes).
- Solver revised. Again made faster and more stable.
- get_row/get_column returned FALSE if the row/column is empty. Fixed.
- get_rowex/get_columnex now returns -1 if and error is detected. This instead of 0.
  This to know the distinction between an empty row/column and an error.
- set_bounds had a possible problem when min and max are equal. Fixed.
- A crash/damage error could occur when rows/columns are added after a solve. Fixed.
- The my_chsign macro in lp_types.h gave warnings with some compilers. Fixed.
- The lp_solve program now returns 255 if an unexpected error occurs. Before this was 1
  But this interferes with the lpsolve library return codes.
- With the lp_solve program, debug and print modes were not written correctly in a
  specified parameter file. Fixed.
- With the lp_solve program, presolveloops was not set correctly. Fixed.

17/09/05 version 5.5.0.5
- In some cases, SOS restrictions were not optimized till optimality. Fixed.
- Presolve sometimes generated 'Column -xxx out of range during presolve' with a possible crash.
- Presolve sometimes removed integer and/or semi-cont variables that should not be deleted. Fixed.
- B&B sometimes didn't find the most optimal solution. Fixed.
- Internal constant COMP_EQUAL renamed to COMP_PREFERNONE because this could interfere with a define
  in the standard header files.
- The lp parser had problems with variables starting with INF and there is a + or - sign just before it.
  Fixed.
- Added options -presolvem, -presolvefd, -presolvebnd, -presolved, -presolveslk
- Updated documentation. put_bb_branchfunc, put_bb_nodefunc, set_epslevel, dualize_lp, set_basisvar

We are thrilled to hear from you and your experiences with this new version. The good and the bad.
Also we would be pleased to hear about your experiences with the different BFPs on your models.

Please send reactions to:
Peter Notebaert: lpsolve@peno.be
Kjell Eikland: kjell.eikland@broadpark.no
