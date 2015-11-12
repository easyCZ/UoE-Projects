#!/usr/bin/python
# counter.py
import sys
import heapq


MOST_COMMON_THRESHOLD = 10
most_common = []
heapq.heapify(most_common)

# Approach:
#   * Will receive 10 entries per each mapper
#   * Runs only 1 reducer
#   * Keeps track of the 10 most popular in memory
#   * Outputs the 10 most popular
for line in sys.stdin:
    post_id, count = line.split('\t', 1)
    count = int(count)
    count_id = (count, post_id)

    if len(most_common) >= MOST_COMMON_THRESHOLD:
        heapq.heappushpop(most_common, count_id)
    else:
        heapq.heappush(most_common, count_id)

# Print the heap
for count, post_id in heapq.nlargest(MOST_COMMON_THRESHOLD, most_common):
    print('{0}, {1}'.format(post_id, count))