#!/usr/bin/python

import sys

for line in sys.stdin:
    line = line.strip()
    print("%s\t%d" % (line, 1))
