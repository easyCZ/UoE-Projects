#!/usr/bin/python
import os
import sys


filename = 'failed'
try:
    filename = os.environ['mapreduce_map_input_file']
except:
    pass

for line in sys.stdin:
    pass

print filename