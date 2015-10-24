#!/usr/bin/python
import sys
from ast import literal_eval

MARK_SHORT = 'M'
STUDENT_SHORT = 'S'

last_sid = None
student_name = None
marks = []


def write(name, course_grades):
    """
    Writes the student's name and the grades formated as per spec
    """
    if not marks: return
    print_grades = " ".join(["({0},{1})".format(course, grade) for (course, grade) in course_grades])
    print("{0} --> {1}".format(student_name, print_grades))

# The input is expected to be sorted primarily on ID
# and secondarily on S or M in reverse to receive
# the student name before the marks

# Expected input:
#   <id_1>    <S> <student name>
#   <id_1>    <M> <list of marks>
#   <id_1>    <M> <list of marks>
#   <id_2>    <S> <student name>
#   <id_2>    <M> <list of marks>

for line in sys.stdin:
    sid, relation, data = line.strip().split('\t', 2)

    if last_sid and last_sid != sid:
        write(student_name, marks)
        marks = []

    last_sid = sid

    if relation == STUDENT_SHORT:
        student_name = data
    else:
        # Aggregate marks
        marks = marks + literal_eval(data)

# Write last set of records
write(student_name, marks)
