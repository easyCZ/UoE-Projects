#!/usr/bin/python
# mapper.py
import sys
import xml.etree.ElementTree as xml


POST_TYPE_ID = 'PostTypeId'
PARENT_ID = 'ParentId'
ANSWER_USER_ID = 'OwnerUserId'

ANSWER_TYPE_ID = '2'


def is_answer(post_type): return post_type == ANSWER_TYPE_ID

for line in sys.stdin:
    content = xml.fromstring(line.strip())
    attributes = content.attrib

    if is_answer(attributes[POST_TYPE_ID]):
        user = attributes.get(ANSWER_USER_ID, None)
        post_id = attributes.get(PARENT_ID, None)

        # Skip results without proper attributes
        if user and post_id:
            print('{0}\t{1}'.format(user, post_id))
        else:
            sys.stderr.write("Could not retrieve name and post_id in {0}\n".format(attributes))
