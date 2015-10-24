#!/usr/bin/python

import sys

word_count = 0
line_count = 0

for line in sys.stdin:
    words, lines = line.strip().split('\t')
    word_count += int(words)
    line_count += int(lines)

print("{0}\t{1}".format(word_count, line_count))