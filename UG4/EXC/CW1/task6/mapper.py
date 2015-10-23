#!/usr/bin/python
import sys
import heapq

most_common = []
MOST_COMMON_THRESHOLD = 20
heapq.heapify(most_common)

for line in sys.stdin:
    count, word_pair = line.strip().split('\t', 1)
    count = int(count)

    if len(most_common) >= MOST_COMMON_THRESHOLD:
        heapq.heappushpop(most_common, (count, word_pair))
    else:
        heapq.heappush(most_common, (count, word_pair))


for (count, word_pair) in heapq.nlargest(MOST_COMMON_THRESHOLD, most_common):
    print("{0}\t{1}".format(count, word_pair))
