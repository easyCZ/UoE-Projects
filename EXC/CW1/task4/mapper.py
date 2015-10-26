#!/usr/bin/python
# mapper.py
import sys
from collections import defaultdict

for line in sys.stdin:
    tokens = line.strip().split()
    mapping = defaultdict(list)
    pairs = zip(tokens[:-1], tokens[1:])

    for a, b in pairs:
        mapping[a].append(b)

    for (key, values) in mapping.iteritems():
        print("{0}\t{1}".format(key, ",".join(values)))
