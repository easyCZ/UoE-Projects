for b in 2 4 8 16
do
    python3.5 simulator.py -p mesi -f trace1.txt -b $b
    echo "\n"
    python3.5 simulator.py -p mesi -f trace2.txt -b $b
    echo "\n"
    echo "\n"
done