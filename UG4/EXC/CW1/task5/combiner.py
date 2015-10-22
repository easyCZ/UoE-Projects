#!/usr/bin/python
import sys
from collections import Counter
from ast import literal_eval


MAX_COUNTER_SIZE = 100

counter = Counter()
counter_size = 0

last_key = ""
counter = None

def write(key, counter):
    """
    Write data into stdout if we have iterated something
    """
    if not last_key: return
    print("{0}\t{1}".format(key, list(counter.items())))


def spill(key, counter):
    write(key, counter)
    counter = Counter()


# Reduce
for line in sys.stdin:
    line = line.strip()

    key, values = line.split('\t', 1)
    values = dict(literal_eval(values))
    counter_size += sum(values.values())

    if key != last_key:
        spill(last_key, counter)
        last_key = key
        counter = Counter(values)

    else:
        # Spill records before we run out of memory
        if sum(counter.values())  > MAX_COUNTER_SIZE:
            spill(last_key, counter)
        counter.update(values)

# Dump data
write(last_key, counter)