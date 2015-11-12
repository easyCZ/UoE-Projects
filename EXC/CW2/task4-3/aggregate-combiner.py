#!/usr/bin/python
# aggregate-combiner.py
import sys

answers = []
last_user_id = None

MEM_THRESHOLD = 1024 * 1024 * 1024 # 1 GB

def write(user, entries):
    if len(entries) > 0:
        print('{0}\t{1}'.format(user, ','.join(entries)))

for line in sys.stdin:
    user_id, answer_id = line.strip().split('\t', 1)

    if (last_user_id and last_user_id != user_id) or sys.getsizeof(answers) > MEM_THRESHOLD:
        write(last_user_id, answers)
        answers = []

    last_user_id = user_id
    answers.append(answer_id)

if last_user_id:
    write(last_user_id, answers)
