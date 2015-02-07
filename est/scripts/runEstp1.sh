bendir=/home/duy/chronosTools/source-4.1/benchmarks
estDir=/home/duy/chronosTools/source-4.1/est/est
configDir=/home/duy/chronosTools/source-4.1/est/processor.opt
solver="/home/duy/chronosTools/lp_solve_5.5/lp_solve/lp_solve -rxli /home/duy/chronosTools/lp_solve_5.5/lp_solve/xli_CPLEX"

benarr[0]=../benchmarks/adpcm/adpcm
benarr[1]=../benchmarks/cnt/cnt1.c.bin
benarr[2]=../benchmarks/matmult/matmult.c.bin
benarr[3]=../benchmarks/bsort100/bsort100.c.bin
benarr[4]=../benchmarks/insertsort/insertsort.c.bin
benarr[5]=../benchmarks/fdct/fdct.c.bin
#benarr[6]=../benchmarks/jfdctint/jfdctint.c.bin

dumptxt=dump.txt
tempfile=temp.txt

readObsWCET()
{

grep "effective sim cycles" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
obsWCET=$5

rm $tempfile
rm $dumptxt
}

readEstWCET(){

grep "function" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
estWCET=$5

rm $tempfile
}

args=""


while read line ;do
	#echo $line
	length=${#line}
	#echo $length
	
	index=0
	flag=1
	while [ $index -le 10 ]
	do
		substr=${line:index:1}
		#echo $index ${line:index:1}
		index=$(($index  + 1))
		if [ "$substr" = "#" ]
		then
			flag=0
			break
		fi
	done

	if [ $flag -eq 1 ]
	then
		args=$args" "$line
	fi

done < $configDir

echo $args > args.txt

benname=""
obsWCET=0
runBenchmark()
{
~/chronosTools/simplesim-3.0/sim-outorder  $args $benname > $dumptxt
cat $dumptxt > simdump.txt
readObsWCET
}

runEst()
{
$estDir -config $configDir $benname #> $dumptxt
}

runSolver()
{
$solver $benname.lp > $dumptxt
readEstWCET
rm $dumptxt
}

echo parameter: $#
if [ $# -eq 0 ]
then
	index=0
	length=${#benarr[*]}
	for ben in ${benarr[*]}
	do
		benname=$ben
		runBenchmark
		runEst
		runSolver
		obsWCETarr[$index]=$obsWCET
		estWCETarr[$index]=$estWCET
		index=$(($index  + 1))
	done


	echo "*******************************************************************"
	echo
	output=wcet.txt
	index=0
	while [ $index -lt $length ]
	do
		#echo obsWCET ${benarr[$index]} : ${obsWCETarr[$index]}
		echo -e "obsWCETarr[$index]"  = ${obsWCETarr[$index]}"\t\t\t#${benarr[$index]}"
		index=$(($index + 1))
	done
	echo	
	index=0
	while [ $index -lt $length ]
	do
		echo -e "estWCETarr[$index]"  = ${estWCETarr[$index]}"\t\t\t#${benarr[$index]}"
		index=$(($index + 1))
	done
	echo
	echo "est/obs"
	index=0
	while [ $index -lt $length ]
	do
		a=$(printf "%f" ${estWCETarr[$index]})
		a=$(echo "scale=2;$a * 100"|bc)
		b=${obsWCETarr[$index]}
		result=$(echo "scale=2;$a/$b"|bc)
		#echo -e ${estWCETarr[$index]}" / "${obsWCETarr[$index]} "\t= $result%""\t\t\t#${benarr[$index]}"
		echo -e $result%"\t\t\t#${benarr[$index]}"
		index=$(($index + 1 ))
	done	
	#####################################################
	echo $length > $output
	for ben in ${benarr[*]}
	do
		echo $ben >> $output
	done

	index=0
	while [ $index -lt $length ]
	do
		echo ${obsWCETarr[$index]} >> $output
		index=$(($index + 1))
	done

	index=0
	while [ $index -lt $length ]
	do
		echo ${estWCETarr[$index]} >> $output
		index=$(($index + 1))
	done
	exit 0
else
	
	benname=$1
	runBenchmark
	runEst
	runSolver
	echo "*******************************************************************"
	echo
	echo -e obsWCET: $obsWCET "\t#"$benname
	echo -e estWCET: $estWCET "\t#"$benname
	a=$(printf "%f" $estWCET)
	a=$(echo "scale=2;$a * 100" | bc)
	b=$obsWCET
	result=$(echo "scale=2;$a/$b"|bc)
	echo est/obs = $result%
fi



