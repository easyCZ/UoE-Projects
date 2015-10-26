#!/usr/bin/python


import sys

prev_word = ""
value_total = 0
word = ""

for line in sys.stdin:
    line = line.strip()
    word, value = line.split('\t', 1)
    value = int(value)

    if prev_word == word:
        value_total += value

    else:
        if prev_word:
            print("%s\t%i" % (prev_word, value_total))

        value_total = value
        prev_word = word

if prev_word == word:
    print("%s\t%i" % (prev_word, value_total))
