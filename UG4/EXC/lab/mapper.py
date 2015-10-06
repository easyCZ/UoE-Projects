#!/usr/bin/python


import sys

for line in sys.stdin:
    line = line.strip()
    tokens = line.split()

    for token in tokens:
        print("%s\t%i" % (token, 1))
