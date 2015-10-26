#!/usr/bin/python
import sys
from collections import Counter


last_key = ""
counter = None

def write():
    """
    Write data into stdout if we have iterated something
    """
    if not last_key: return
    for second, count in counter.iteritems():
        print("{0}\t{1} {2}".format(count, key, second))


# Reduce
for line in sys.stdin:
    line = line.strip()

    key, values = line.split('\t', 1)
    values = values.split(',')

    if key != last_key:
        write()
        last_key = key
        counter = Counter(values)

    else:
        counter.update(values)

# Dump data
write()