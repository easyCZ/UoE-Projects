#!/usr/bin/python

import sys

sentence = ""
previous = ""

for line in sys.stdin:
    sentence = line.strip()

    if previous != sentence:
        if previous:
            print(previous)

        count = 1
        previous = sentence

if previous == sentence:
    print(previous)