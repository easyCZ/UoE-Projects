#!/usr/bin/python

import sys
import hashlib

for line in sys.stdin:
    line = line.strip()

    key = hashlib.sha256(line).hexdigest()
    print("{0}\t{1}".format(key, line))