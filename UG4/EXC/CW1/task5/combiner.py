#!/usr/bin/python
import sys
from collections import Counter


last_key = ""
counter = None
SPILL_THRESHOLD = 100

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

    if key != last_key or counter.most_common(1)[0][1] > SPILL_THRESHOLD:
        write()
        last_key = key
        counter = Counter(values)
    else:
        counter.update(values)

# Dump data
write()