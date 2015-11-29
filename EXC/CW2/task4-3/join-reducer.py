#!/usr/bin/python
# join-reducer.py
import sys

QUESTION = 'Q'
ANSWER = 'A'


current_question_id = None

# Receives input sorted primarily by id,
# secondarily by Question first then Answer
for line in sys.stdin:
    tokens = line.strip().split()
    _id = tokens[0]

    if len(tokens) == 2:
        # This is a Question row
        current_question_id = _id
    if len(tokens) == 3:
        owner_id = tokens[-1]
        # Answer row - validate against question id
        if current_question_id == _id:
            print('{0}\t{1}'.format(owner_id, _id))


