#!/usr/bin/python
# mapper.py
import sys

STUDENT = 'student'
STUDENT_SHORT = 'S'
MARK = 'mark'
MARK_SHORT = 'M'


for line in sys.stdin:
    line = line.strip()

    if line.startswith(STUDENT):
        relation, sid, name = line.split('   ', 2)
        # print "1   S   George"
        print("{0}\t{1}\t{2}".format(sid, STUDENT_SHORT, name))

    elif line.startswith(MARK):
        relation, course, sid, mark = line.split('   ', 3)
        course_grade = (course, int(mark))

        # print "1  M   [(ADBS, 80)]"
        print("{0}\t{1}\t{2}".format(sid, MARK_SHORT, str(course_grade)))