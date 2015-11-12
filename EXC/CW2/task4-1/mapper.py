#!/usr/bin/python
# mapper.py
import sys
import heapq
import xml.etree.ElementTree as xml


POST_TYPE_ID = 'PostTypeId'
VIEW_COUNT = 'ViewCount'
POST_ID = 'Id'

QUESTION_TYPE_ID = '1'

MOST_COMMON_THRESHOLD = 10
most_common = []
heapq.heapify(most_common)


def is_question(post_type): return post_type == QUESTION_TYPE_ID

for line in sys.stdin:
    content = xml.fromstring(line.strip())
    attributes = content.attrib
    if is_question(attributes[POST_TYPE_ID]):
        view_count = int(attributes[VIEW_COUNT])
        count_id = (view_count, attributes[POST_ID])

        if len(most_common) >= MOST_COMMON_THRESHOLD:
            heapq.heappushpop(most_common, count_id)
        else:
            heapq.heappush(most_common, count_id)

# Print the heap
for count, post_id in heapq.nlargest(MOST_COMMON_THRESHOLD, most_common):
    print('{0}\t{1}'.format(post_id, count))