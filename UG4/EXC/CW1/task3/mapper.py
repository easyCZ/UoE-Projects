#!/usr/bin/python
# mapper.py

import sys

word_count = 0
line_count = 0

for line in sys.stdin:
    line = line.strip()
    words = line.split()
    word_count += int(len(words))
    line_count += 1

print("{0}\t{1}".format(word_count, line_count))