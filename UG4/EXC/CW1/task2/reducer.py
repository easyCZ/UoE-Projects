#!/usr/bin/python
# reducer.py
import sys

key = ""
sentence = ""
previous_key = ""

for line in sys.stdin:
    sentence = line.strip()

    # Handle empty lines
    tokens = sentence.split('\t')
    # Key will always be present
    key = tokens[0]
    # Empty lines will just be joined to empty
    sentence = "\t".join(tokens[1:])

    if previous_key != key:
        print(sentence)
        previous_key = key