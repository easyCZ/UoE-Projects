#!/usr/bin/python
import os
import sys
import string
from collections import Counter

filename = os.environ['mapreduce_map_input_file'].split('/')[-1]

trans = string.maketrans('', '')

for line in sys.stdin:
    line = line.strip().lower()
    # Remove punctuation
    line = line.translate(trans, string.punctuation)
    words = line.split()
    if words:
        counter = Counter(words)

        for (word, count) in counter.iteritems():
            print("{0}\t{1}\t{2}".format(word, filename, count))