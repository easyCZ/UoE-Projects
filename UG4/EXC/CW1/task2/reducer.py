#!/usr/bin/python

import sys

key = ""
sentence = ""
previous_key = ""

for line in sys.stdin:
    sentence = line.strip()
    key, sentence = sentence.split('\t', 1)

    if previous_key != key:
        if previous_key:
            print(sentence)

        previous_key = key


if previous_key != key:
    print(sentence)