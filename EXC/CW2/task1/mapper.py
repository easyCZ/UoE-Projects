#!/usr/bin/python
import os
import sys

try:
    filename = os.environ['map_input_file']
except:
    filename = 'failed'

print filename