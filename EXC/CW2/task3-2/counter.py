#!/usr/bin/python
# counter.py
import os
import sys
import heapq


MOST_COMMON_THRESH = 10
most_common = []
heapq.heapify(most_common)

last_host = None
last_host_count = 0

for line in sys.stdin:
    host, count = line.strip().split('\t', 1)
    count = int(count)

    if last_host != host:
        # Store the new host
        # Keep a heap of most common items
        if len(most_common) >= MOST_COMMON_THRESH:
            heapq.heappushpop(most_common, (last_host_count, last_host))
        else:
            heapq.heappush(most_common, (last_host_count, last_host))

        last_host_count = 0
    
    last_host = host
    last_host_count += count

for (count, host) in heapq.nlargest(MOST_COMMON_THRESH, most_common):
    print("{0}\t{1}".format(host, count))

