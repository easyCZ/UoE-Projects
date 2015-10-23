#!/usr/bin/python
import sys
from collections import Counter, defaultdict


for line in sys.stdin:
    row, values = line.strip().split('\t')
    row_values = values.split('  ')

    # print row, row_values


    for (col, col_value) in enumerate(row_values):
        # out: <col>    <row>   <value>
        print("{0}\t{1}\t{2}".format(col, row, col_value))