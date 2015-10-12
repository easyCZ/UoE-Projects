#!/usr/bin/python

import sys
import hashlib

for line in sys.stdin:
    line = line.strip()

    if line:
        key = hashlib.md5(line).hexdigest()
        print("{0}\t{1}".format(key, line))
