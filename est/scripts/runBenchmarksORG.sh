if [ $# -ne 2 ]
then
echo ...
exit 0
fi
dumptxt=dump.txt
tempfile=temp.txt
args=""
benname=$2
readObsWCET()
{

grep "effective sim cycles" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
obsWCET=$5

rm $tempfile
#rm $dumptxt
}

runBenchmark()
{
~/chronosTools/simplesim-3.0-ORG/sim-outorder  $args $benname 
#readObsWCET
}

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

done < $1

echo

runBenchmark

echo $args
