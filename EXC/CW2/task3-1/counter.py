#!/usr/bin/python
# counter.py
import os
import sys
from collections import namedtuple

MaxCount = namedtuple('MaxCount', ['resource', 'count'])

max_counter = None
resource_counter = 0
last_resource = None


for line in sys.stdin:
    resource, count = line.strip().split('\t', 1)
    count = int(count)

    if max_counter is None:
        max_counter = MaxCount(resource, count)


    if last_resource is not None and resource != last_resource:
        # We received a new resource
        if resource_counter > max_counter.count:
            # We found a new max
            max_counter = MaxCount(last_resource, resource_counter)
        resource_counter = 0

    last_resource = resource
    resource_counter += count



if max_counter:
    if resource_counter > max_counter.count:
        # We found a new max
        max_counter = MaxCount(last_resource, resource_counter)

    print("{0}\t{1}".format(max_counter.resource, max_counter.count))