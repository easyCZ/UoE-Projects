#!/usr/bin/python
import os
import sys
from collections import Counter


filename = os.environ['mapreduce_map_input_file'].split('/')[-1]

last_word = None
accumulator = 0

# Input is received sorted by word
for line in sys.stdin:
    word, count = line.strip().split('\t', 1)

    if word is not None and word != last_word:
        print("{0}\t{1}\t{2}".format(last_word, accumulator, filename))
        accumulator = 0

    last_word = word
    accumulator =+ int(count)

print("{0}\t{1}\t{2}".format(last_word, filename, accumulator))