#!/usr/bin/python
import sys
import heapq

most_common = []
heapq.heapify(most_common)

for line in sys.stdin:
    count, word_pair = line.strip().split('\t', 1)
    count = int(count)

    if len(most_common) >= 10:
        heapq.heappushpop(most_common, (count, word_pair))
    else:
        heapq.heappush(most_common, (count, word_pair))


for (count, word_pair) in heapq.nlargest(10, most_common):
    print("{0}\t{1}".format(count, word_pair))
