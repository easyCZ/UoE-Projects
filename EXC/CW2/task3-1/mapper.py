#!/usr/bin/python
# mapper.py
import sys

# Input:
# in24.inetnebr.com - - [01/Aug/1995:00:00:01 -0400] "GET /shuttle/missions/sts-68/news/sts-68-mcc-05.txt HTTP/1.0" 200 1839
for line in sys.stdin:
    try:
        resource = line.strip().split('"')[1].split()[1]
        print("{0}\t{1}".format(resource, 1))
    except:
        pass
        # sys.stderr.write("Failed to parse, skipping. Line: {0}\n".format(line.strip()))