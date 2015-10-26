#!/usr/bin/python

import sys

word_count = 0
line_count = 0

for line in sys.stdin:
    sentence = line.strip()
    words, lines = sentence.split('\t', 1)
    word_count += int(words)
    line_count += int(lines)

print("{0} {1}".format(word_count, line_count))