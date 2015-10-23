#!/usr/bin/python
import sys
from collections import Counter
from ast import literal_eval


MAX_COUNTER_SIZE = 250

counter = Counter()
counter_size = 0

last_key = ""

def write(key, counter):
    """
    Write data into stdout if we have iterated something
    """
    if not last_key: return
    print("{0}\t{1}".format(key, list(counter.items())))


def spill(key, counts):
    # print "Spill"
    write(key, counts)
    return (Counter(), 0)


# Reduce
for line in sys.stdin:
    line = line.strip()

    key, values = line.split('\t', 1)
    values = dict(literal_eval(values))
    counter_size += sum(values.values())

    # print ">>> Counter Size: %d" % counter_size

    if key != last_key:
        counter, counter_size = spill(last_key, counter)
        last_key = key
        counter.update(values)

    else:
        # Spill records before we run out of memory
        if counter_size  > MAX_COUNTER_SIZE:
            counter, counter_size = spill(last_key, counter)
        counter.update(values)

# Dump data
write(last_key, counter)