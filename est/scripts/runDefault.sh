tempfile=a.txt
errormsg=b.txt
CDIR=/home/duy/Desktop/chronos-4.1/est
readObsWCET(){
grep "effective sim cycles" temp.txt > $tempfile

set $(grep -v sec $tempfile)
echo obs WCET: $5

rm $tempfile
}

readEstWCET(){

grep "function" temp.txt > $tempfile

set $(grep -v sec $tempfile)
echo est WCET: $5

rm $tempfile
}
$CDIR/est -config processor.opt ../benchmarks/adpcm/adpcm
$CDIR/est -config processor.opt ../benchmarks/bsort100/bsort100.c.bin 
$CDIR/est -config processor.opt ../benchmarks/cnt/cnt1.c.bin
$CDIR/est -config processor.opt ../benchmarks/matmult/matmult.c.bin 
$CDIR/est -config processor.opt ../benchmarks/insertsort/insertsort.c.bin
$CDIR/est -config processor.opt ../benchmarks/fdct/fdct.c.bin


echo adpcm
#./runSim ../benchmarks/adpcm/adpcm > temp.txt 
#readObsWCET
./lpsolver ../benchmarks/adpcm/adpcm.lp > temp.txt
readEstWCET
echo 

echo bsort100
#./runSim ../benchmarks/bsort100/bsort100.c.bin > temp.txt
#readObsWCET
./lpsolver ../benchmarks/bsort100/bsort100.c.bin.lp > temp.txt
readEstWCET
echo

echo cnt1
#./runSim ../benchmarks/cnt/cnt1.c.bin > temp.txt
#readObsWCET
./lpsolver ../benchmarks/cnt/cnt1.c.bin.lp > temp.txt
readEstWCET
echo

echo matmult
#./runSim ../benchmarks/matmult/matmult.c.bin > temp.txt
#readObsWCET
./lpsolver ../benchmarks/matmult/matmult.c.bin.lp > temp.txt
readEstWCET
echo

echo insertsort
#./runSim ../benchmarks/insertsort/insertsort.c.bin > temp.txt
#readObsWCET
./lpsolver ../benchmarks/insertsort/insertsort.c.bin.lp > temp.txt
readEstWCET
echo

echo fdct
./lpsolver ../benchmarks/fdct/fdct.c.bin.lp > temp.txt
readEstWCET
echo

