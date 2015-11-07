#!/usr/bin/python
# mapper.py
import os
import sys

# Requires the number of files to map to be part of the input
FILE_COUNT = int(sys.argv[1])
TERMS = set()

# Build dictionary
with open('terms.txt') as f:
    for term in f:
        term = term.strip()
        TERMS.add(term)


# Input pattern:
# "And : 151 : {(d1.txt, 34), (d10.txt, 4), (d12.txt, 25), (d13.txt, 5), (d15.txt, 19), (d2.txt, 17)}
for line in sys.stdin:
    pass

print(TERMS)
    # word, total_count, documents = line.strip().split(' : ', 2)
    # documents = documents[1:-1].split(', ')
    # documents = []

    # if words:
    #     counter = Counter(words)

    #     for (word, count) in counter.iteritems():
    #         print("{0} {1} {2}".format(word, filename, count))
