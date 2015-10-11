#!/usr/bin/python

import sys


for line in sys.stdin:
    # Need to strip away newline chars
    print(line.strip().lower())
