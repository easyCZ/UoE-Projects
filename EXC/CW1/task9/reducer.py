#!/usr/bin/python
import sys
from collections import Counter
from ast import literal_eval


min_average = None
student_names = []

# Reduce
for line in sys.stdin:
    name, average = line.strip().split('\t', 1)
    average = float(average)

    if not min_average:
        min_average = average
        student_names = []

    if average < min_average:
        # We've got a new winner
        min_average = average
        student_names = [name]

    elif average == min_average:
        student_names.append(name)

for name in student_names:
    print("{0} with {1}".format(name, "%.1f" % min_average))