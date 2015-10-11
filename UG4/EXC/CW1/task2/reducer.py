#!/usr/bin/python

import sys

sentence = ""
count = 0
previous = ""

for line in sys.stdin:
    sentence = line.strip()

    if previous == sentence:
        count += 1
    else:
        if previous:
            print(previous)

        count = 1
        previous = sentence

if previous == sentence:
    print(previous)