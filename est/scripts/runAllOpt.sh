
runID=$RANDOM
sim_dir=/home/duy/chronosTools/simulator/simplesim-3.0/sim-outorder
solver="/home/duy/chronosTools/lp_solve_5.5/lp_solve/lp_solve -rxli /home/duy/chronosTools/lp_solve_5.5/lp_solve/libxli_CPLEX.so"
estDir=$(pwd)/est
dumptxt=dump$runID.txt
tempfile=temp$runID.txt

opt_dir=$1
ben_name=$2
ls_file=ls_$runID.txt
output_file=run_all_opt.txt
output_est=run_all_est.txt
output_obs=run_all_sim.txt

readObsWCET()
{

grep "effective sim cycles" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
obsWCET=$5

rm $tempfile
rm $dumptxt
}

runBenchmark()
{
$sim_dir  -config $opt $ben_name > $dumptxt
readObsWCET
}

runEst()
{
rm $ben_name.lp
rm $ben_name.ilp
$estDir -config $opt $ben_name #> $dumptxt
}
readEstWCET(){

grep "function" $dumptxt > $tempfile

set $(grep -v sec $tempfile)
estWCET=$5

rm $tempfile
}

runSolver()
{
$solver $ben_name.lp > $dumptxt
readEstWCET
rm $dumptxt
}

ls $opt_dir/*/*.opt > $ls_file
echo -n > $output_file
while read opt
do
	temp=$(dirname $opt)
	basename $temp >> $output_file	
done < $ls_file

echo >> $output_file
while read opt
do
	cache_size=$(basename $opt .opt)
	echo $cache_size >> $output_file
done < $ls_file

index=0
while read opt
do
	runBenchmark
	obsWCETarr[$index]=$obsWCET

	runEst
	runSolver
	estWCETarr[$index]=$estWCET	

	index=$(($index  + 1))
done < $ls_file



echo "*********************************************************************"
echo -n > $output_obs
for wcet in ${obsWCETarr[*]}
do
	echo $wcet >> $output_obs
done

echo -n > $output_est
for wcet in ${estWCETarr[*]}
do
	echo $wcet >> $output_est
done

echo "successfully exit"
rm $ls_file

