#!/usr/bin/python

import sys

try:
    key = ""
    sentence = ""
    previous_key = ""

    for line in sys.stdin:
        sentence = line.strip()
        key, sentence = sentence.split('\t', 1)

        print key, sentence

        if previous_key != key:
            if previous_key:
                print(sentence)

            previous_key = key


    if previous_key != key:
        print(sentence)

except Exception, e:
    sys.stderr.write(e)
    sys.exit(1)