flag=0
length=0
index=0
while read line; do
	#echo $line
	if [ $flag -eq 0 ]
	then
		flag=1;
		length=$line
	elif [ $flag -eq 1 ]
	then
		benarr[$index]=$line
		index=$(($index+1))
		if [ $index -eq $length ]
		then
			index=0
			flag=2
		fi
	elif [ $flag -eq 2 ]
	then
		obsWCETarr[$index]=$line
		index=$(($index+1))
		if [ $index -eq $length ]
		then
			index=0
			flag=3
		fi
	elif [ $flag -eq 3 ]
	then
		oldestWCETarr[$index]=$line
		index=$(($index+1))
	fi
done < wcet.txt

estDir=/home/duy/chronosTools/source-4.1/est/est
configDir=/home/duy/chronosTools/source-4.1/est/processor.opt
solver="/home/duy/chronosTools/lp_solve_5.5/lp_solve/lp_solve -rxli /home/duy/chronosTools/lp_solve_5.5/lp_solve/xli_CPLEX"
dumptxt=dump.txt
tempfile=temp.txt

searchBenchmark()
{
	index=0
	benindex=-1
	for ben in ${benarr[*]}
	do
		if [ "$ben" = "$benname" ]
		then
			benindex=index
			break
		fi
		index=$(($index+1))
	done
	if [ $benindex -lt 0 ]
	then
		echo "no benchmark"
		exit 0
	fi
}

runEst()
{
$estDir -config $configDir $benname #> $dumptxt
}

readEstWCET(){

grep "function" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
estWCET=$5

rm $tempfile
}

runSolver()
{
$solver $benname.lp > $dumptxt
readEstWCET
rm $dumptxt
}

if [ $# -eq 0 ]
then
	index=0
	length=${#benarr[*]}
	for ben in ${benarr[*]}
	do
		benname=$ben
		searchBenchmark
		runEst
		runSolver
		obsWCETarr[$index]=${obsWCETarr[$benindex]}
		estWCETarr[$index]=$estWCET
		index=$(($index  + 1))
	done
	echo "*******************************************************************"
	echo
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
		a=$(printf "%f" ${oldestWCETarr[$index]})
		a=$(echo "scale=2;$a * 100"|bc)
		result2=$(echo "scale=2;$a/$b"|bc)
		echo -e $result% "($result2%)""\t\t\t#${benarr[$index]}"
		index=$(($index + 1 ))
	done	
	exit 0
else
	benname=$1
	searchBenchmark
	runEst
	runSolver
	echo "*******************************************************************"
	echo
	obsWCET=${obsWCETarr[$benindex]}
	echo -e obsWCET: $obsWCET "\t#"$benname
	echo -e newestWCET: $estWCET "\t#"$benname
	a=$(printf "%f" $estWCET)
	a=$(echo "scale=2;$a * 100" | bc)
	b=$obsWCET
	result=$(echo "scale=2;$a/$b"|bc)
	a=$(printf "%f" ${oldestWCETarr[$benindex]})
	a=$(echo "scale=2;$a * 100" | bc)
	result2=$(echo "scale=2;$a/$b"|bc)
	echo est/obs = $result% "($result2%)"
fi

