#!/usr/bin/python

import sys

for line in sys.stdin:
    line = line.strip()
    words = line.split()

    print("count\t{0}\t{1}".format(len(words), 1))