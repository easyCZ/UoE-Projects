#!/usr/bin/python
import os
import sys
from collections import Counter

filename = os.environ['mapreduce_map_input_file'].split('/')[-1]

last_word = None
accumulator = 0

# Input is received sorted by word
for line in sys.stdin:
    line = line.strip()

    word, count = line.split('\t', 1)

    if last_word is not None and word != last_word:
        print("{0}\t{1}\t{2}".format(last_word, filename, accumulator))
        accumulator = 0

    last_word = word
    accumulator += int(count)


if last_word is not None:
    print("{0}\t{1}\t{2}".format(last_word, filename, accumulator))