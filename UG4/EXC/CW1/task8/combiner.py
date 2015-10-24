#!/usr/bin/python
import sys
from ast import literal_eval

MARK_SHORT = 'M'

last_sid = None
marks = []

# Input is sorted by <id> and secondarily by S|M in reverse
# so that Student name comes before the marks fot that student
# Expected input:
#   <id_1>  S   <student name>
#   <id_1>  M   (<course name>, <grade>)
#   <id_1>  M   (<course name>, <grade>)
#   <id_1>  M   (<course name>, <grade>)
#   <id_2>  S   <student name>
#   <id_2>  M   (<course name>, <grade>)
for line in sys.stdin:
    sid, relation, data = line.strip().split('\t', 2)

    if last_sid and last_sid != sid:
        print("{0}\t{1}\t{2}".format(last_sid, MARK_SHORT, str(marks)))
        marks = []
        last_sid = sid

    if relation == MARK_SHORT:
        # Aggregate marks
        course_grade = literal_eval(data)
        marks.append(course_grade)
        last_sid = sid

    else:
        print(line.strip())

print("{0}\t{1}\t{2}".format(sid, MARK_SHORT, str(marks)))

# Expected output:
# 1   S   George
# 1   M   [('TTS', 80), ('EXC', 70), ('ADBS', 80)]
# 2   S   Anna
# 2   M   [('EXC', 65)]
