#!/usr/bin/python
import os
import sys
from collections import Counter

filename = os.environ['mapreduce_map_input_file'].split('/')[-1]

for line in sys.stdin:
    words = line.strip().split()
    if words:
        counter = Counter(words)

        for (word, count) in counter.iteritems():
            print("{0} {1} {2}".format(word, filename, count))
