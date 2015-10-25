#!/usr/bin/python
# mapper.py
import sys
from collections import Counter, defaultdict

ARROW = ' --> '
MARK_SIZE_MIN = 4

min_average = None
student_names = []

for line in sys.stdin:
    name, marks = line.strip().split(ARROW)

    tuples = marks.split(' ')
    pairs = [ p[1:-1].split(',', 1) for p in tuples]

    if len(pairs) > MARK_SIZE_MIN:
        marks = [(course, int(grade)) for (course, grade) in pairs]
        average = sum([grade for (course, grade) in marks]) / len(marks)

        if not min_average:
            min_average = average
            student_names = [name]

        if average < min_average:
            # We've got a new winner
            min_average = average
            student_names = [name]

        elif average == min_average:
            student_names.append(name)

for name in student_names:
    print("{0}\t{1}".format(name, min_average))