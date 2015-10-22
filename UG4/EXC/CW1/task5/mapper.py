#!/usr/bin/python
import sys
from collections import Counter, defaultdict


for line in sys.stdin:
    tokens = line.strip().split()
    mapping = defaultdict(Counter)
    pairs = zip(tokens[:-1], tokens[1:])

    for a, b in pairs:
        mapping[a].update([b])

    for (key, counter) in mapping.iteritems():
        # prints "mary  [('had', 1)]"
        print("{0}\t{1}".format(key, list(counter.items())))
