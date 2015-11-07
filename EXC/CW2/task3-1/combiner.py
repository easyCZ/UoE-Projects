#!/usr/bin/python
# combiner.py
import os
import sys

last_word = None
last_filename = None
accumulator = 0

# Input is received sorted by word and by filename
for line in sys.stdin:
    line = line.strip()

    word, filename, count = line.split(' ', 2)

    if last_word is not None and word != last_word:
        print("{0} {1} {2}".format(last_word, last_filename, accumulator))
        accumulator = 0

    last_word = word
    last_filename = filename
    accumulator += int(count)


if last_word is not None:
    print("{0} {1} {2}".format(last_word, last_filename, accumulator))