#!/usr/bin/python
# mapper.py
import os
import sys

INPUT_FILES = os.environ['mapreduce_input_fileinputformat_inputdir']
mapreduce.task.tmp.dir
# Input pattern:
# "And : 151 : {(d1.txt, 34), (d10.txt, 4), (d12.txt, 25), (d13.txt, 5), (d15.txt, 19), (d2.txt, 17), (d3.txt, 34), (d4.txt, 1), (d5.txt, 4), (d6.txt, 1), (d7.txt, 1), (d9.txt, 6)}
for line in sys.stdin:
    print(INPUT_FILES)
    # word, total_count, documents = line.strip().split(' : ', 2)
    # documents = documents[1:-1].split(', ')
    # documents = []

    # if words:
    #     counter = Counter(words)

    #     for (word, count) in counter.iteritems():
    #         print("{0} {1} {2}".format(word, filename, count))
