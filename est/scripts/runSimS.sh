bendir=/home/duy/chronosTools/source-4.1/benchmarks
#estDir=/home/duy/chronosTools/source-4.1/estUN_MUST/est
estDir=$(pwd)/est
configDir=$1
solver="/home/duy/chronosTools/lp_solve_5.5/lp_solve/lp_solve -rxli /home/duy/chronosTools/lp_solve_5.5/lp_solve/libxli_CPLEX.so"
runID=$RANDOM

benarr[0]=../new_benchmarks/adpcm/adpcm
benarr[1]=../new_benchmarks/cnt/cnt
benarr[2]=../new_benchmarks/matmult/matmult
benarr[3]=../new_benchmarks/bsort100/bsort100
benarr[4]=../new_benchmarks/insertsort/insertsort
benarr[5]=../new_benchmarks/fdct/fdct
benarr[6]=../new_benchmarks/fir/fir
benarr[7]=../new_benchmarks/edn/edn
benarr[8]=../new_benchmarks/lms/lms
benarr[9]=../new_benchmarks/bs/bs
benarr[10]=../new_benchmarks/expint/expint
benarr[11]=../new_benchmarks/fft/fft
#benarr[6]=../benchmarks/jfdctint/jfdctint.c.bin

dumptxt=dump$runID.txt
tempfile=temp$runID.txt

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

#echo $args

benname=""
obsWCET=0
runBenchmark()
{
/home/duy/chronosTools/simulator/simplesim-3.0/sim-outorder  $args $benname > $dumptxt
cat $dumptxt > simdump.txt
readObsWCET
}

runEst()
{
rm $benname.lp
rm $benname.ilp
$estDir -config $configDir $benname #> $dumptxt
}

runSolver()
{
$solver $benname.lp > $dumptxt
readEstWCET
rm $dumptxt
}

echo parameter: $#
if [ $# -eq 1 ]
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
	output=$(basename $(dirname $configDir))_$(basename $configDir)"_wcet.txt"
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
	echo >> $output
	index=0
	while [ $index -lt $length ]
	do
		echo ${estWCETarr[$index]} >> $output
		index=$(($index + 1))
	done
	exit 0
elif [ $# -eq 2 ]
then	
	benname=$2
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

echo $args

