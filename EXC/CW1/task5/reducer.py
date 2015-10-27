#!/usr/bin/python
# reducer.py
import sys
from collections import Counter
from ast import literal_eval


counter = Counter()
counter_size = 0

last_key = ""
counter = None

def write(key, counter):
    """
    Write data into stdout if we have iterated something
    """
    if not last_key: return
    for (second, value) in counter.iteritems():
        print("{0}\t{1} {2}".format(value, key, second))


for line in sys.stdin:
    line = line.strip()

    key, values = line.split('\t', 1)
    values = dict(literal_eval(values))

    if key != last_key:
        write(last_key, counter)
        last_key = key
        counter = Counter(values)

    else:
        counter.update(values)

write(last_key, counter)