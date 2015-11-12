#!/usr/bin/python
# counter.py
import sys
from collections import namedtuple

UserPosts = namedtuple('UserPosts', ['user', 'posts'])


so_superhero = None
current_user = None

for line in sys.stdin:
    user_id, posts = line.strip().split('\t', 1)
    posts = posts.split(', ')

    if not so_superhero:
        # Init
        so_superhero = UserPosts(user_id, [])
        current_user = UserPosts(user_id, [])

    if user_id != current_user.user:
        # Got a new user segment

        # Do we have a new hero?
        if len(current_user.posts) > len(so_superhero.posts):
            so_superhero = current_user

        current_user = UserPosts(user_id, [])

    current_user.posts.extend(posts)

if so_superhero:
    print('{0} -> {1}'.format(so_superhero.user, ', '.join(so_superhero.posts)))