#!/usr/bin/python

import sys

for line in sys.stdin:
    tokens = line.strip().split()
    mapping = {}
    pairs = zip(tokens[:-1], tokens[1:])

    for a, b in pairs:
        if a in mapping:
            mapping[a].append(b)
        else:
            mapping[a] = [b]

    for (key, values) in mapping.iteritems():
        print("{0}\t{1}".format(key, ",".join(values)))
