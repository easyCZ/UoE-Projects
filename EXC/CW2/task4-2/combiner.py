#!/usr/bin/python
# mapper.py
import sys

MEMORY_THRESHOLD = 1024 * 1024 * 1024 # 1 GB

last_user_id = None
question_ids = set()

def write(user, questions):
    if len(questions) > 0:
        print('{0}\t{1}'.format(user, ', '.join(questions)))

for line in sys.stdin:
    user_id, question_id = line.strip().split('\t' ,1)

    if last_user_id and user_id != last_user_id:
        write(last_user_id, question_ids)
        question_ids = set()

    if sys.getsizeof(question_ids) >= MEMORY_THRESHOLD:
        write(last_user_id, question_ids)
        question_ids = set()

    last_user_id = user_id
    question_ids.add(question_id)

if last_user_id:
    write(last_user_id, question_ids)