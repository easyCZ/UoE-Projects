#!/usr/bin/python

import sys


key = ""
sentence = ""
previous_key = ""

for line in sys.stdin:
    sentence = line.strip()

    tokens = sentence.split('\t')
    key = tokens[0]
    sentence = "\t".join(tokens[1:])

    if previous_key != key:
        if previous_key:
            print(sentence)

        previous_key = key


if previous_key != key:
    print(sentence)