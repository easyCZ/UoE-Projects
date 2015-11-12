#!/usr/bin/python
# aggregate-reducer.py
import sys
from collections import namedtuple

UserAnswers = namedtuple('UserAnswers', ['user', 'answers'])

hero_user = None
last_user = None

for line in sys.stdin:
    user, answers = line.strip().split('\t', 1)
    answers = answers.split(',')

    if not hero_user:
        # Init
        hero_user = UserAnswers(user, [])
        last_user = UserAnswers(user, [])

    if last_user.user != user:
        # New set of rows

        # Is it the new hero?
        if len(last_user.answers) > len(hero_user.answers):
            hero_user = last_user

        last_user = UserAnswers(user, [])

    last_user.answers.extend(answers)

if hero_user:
    print('{0} -> {1}, {2}'.format(hero_user.user, len(hero_user.answers), ', '.join(hero_user.answers)))



