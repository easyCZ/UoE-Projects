#!/usr/bin/python

import sys

sentence = ""
count = 0
previous = ""

for line in sys.stdin:
    line = line.strip()
    sentence, value = line.split('\t', 1)
    value = int(value)

    if previous == sentence:
        count += value
    else:
        if previous:
            print(previous)

        count = value
        previous = sentence

if previous == sentence:
    print(previous)