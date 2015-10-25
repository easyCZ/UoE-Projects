#!/usr/bin/python
# mapper.py
import sys
import os

print "TaskCount:", os.environ["mapred.reduce.tasks"]

# for line in sys.stdin:
#     row, values = line.strip().split('\t')
#     row_values = values.split('  ')

#     for (col, col_value) in enumerate(row_values):
#         # out: <col>    <row>   <value>
#         print("{0}\t{1}\t{2}".format(col, row, col_value))