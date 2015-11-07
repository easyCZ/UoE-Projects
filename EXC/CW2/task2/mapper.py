#!/usr/bin/python
# mapper.py
import os
import sys
import math

# Requires the number of files to map to be part of the input
DOCUMENT_COUNT = int(sys.argv[1])
FILENAME = sys.argv[2]
TERMS = set()

# Build dictionary
with open('terms.txt') as f:
    for term in f:
        term = term.strip()
        TERMS.add(term)

# Input pattern:
# "And : 151 : {(d1.txt, 34), (d10.txt, 4), (d12.txt, 25), (d13.txt, 5), (d15.txt, 19), (d2.txt, 17)}
for line in sys.stdin:
    word, total_count, documents = line.strip().split(' : ', 2)

    # We only care about the items in our dictionary
    if word in TERMS:
        # Serialize the freqencies into workable format
        documents = [document.replace('(', '').replace(')', '') for document in documents[1:-1].split('), ')]
        documents = [tuple(document.split(', ')) for document in documents]

        # Make documents a dict for instant lookup
        documents = dict([(document, int(count)) for document, count in documents])

        tf = documents.get(FILENAME, 0)
        idf = math.log(DOCUMENT_COUNT / (1.0 + len(documents)), 10)
        tf_idf = tf * idf
        print("{0}, {1} = {2}".format(word, FILENAME, tf_idf))
