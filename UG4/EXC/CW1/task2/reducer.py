#!/usr/bin/python

import sys

sentence = ""
count = 0
previous = ""

for line in sys.stdin:
    line = line.strip()
    sentence, value = line.split('\t', 1)

    try:
        value = int(value)

            if previous == sentence:
            count += value
        else:
            if previous:
                print(previous)

            count = value
            previous = sentence
    except ValueError:
        pass



if previous == sentence:
    print(previous)