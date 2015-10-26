#!/usr/bin/python
# reducer.py
import sys

last_row = None
row_values = []

# Reduce
for line in sys.stdin:
    row, col, value = line.strip().split('\t')

    if row != last_row and last_row:
        print("{0}".format(" ".join(row_values)))
        row_values = [value]
        last_row = row
    else:
        last_row = row
        row_values.append(value)

print("{0}".format(" ".join(row_values)))