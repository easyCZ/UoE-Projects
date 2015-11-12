#!/usr/bin/python
# mapper.py
import sys
import xml.etree.ElementTree as xml

POST_TYPE_ID = 'PostTypeId'
OWNER_USER_ID = 'OwnerUserId'
ACCEPTED_ANSWER = 'AcceptedAnswerId'
ID = 'Id'

ANSWER_TYPE_ID = '2'

QUESTION = 'Q'
ANSWER = 'A'

def is_answer(post_type): return post_type == ANSWER_TYPE_ID

for line in sys.stdin:
    content = xml.fromstring(line.strip())
    attributes = content.attrib


    if is_answer(attributes[POST_TYPE_ID]):
        # Print the answer set
        # Outputs: <answer_id>  A   <user_id>
        user_id = attributes.get(OWNER_USER_ID, None)
        answer_id = attributes.get(ID, None)

        # Skip results without proper attributes
        if user_id and answer_id:
            print('{0}\t{1}\t{2}'.format(answer_id, ANSWER, user_id))
        else:
            sys.stderr.write("Could not retrieve name and answer_id.\n")
    else:
        # Print the ID of the accepted answer and type
        # Outputs: <accepted_answer_id> Q
        accepted_answer_id = attributes.get(ACCEPTED_ANSWER, None)

        if accepted_answer_id:
            print('{0}\t{1}'.format(accepted_answer_id, QUESTION))



