#!/usr/bin/python
# mapper.py
import sys

# Input:
# in24.inetnebr.com - - [01/Aug/1995:00:00:01 -0400] "GET /shuttle/missions/sts-68/news/sts-68-mcc-05.txt HTTP/1.0" 200 1839
for line in sys.stdin:
    try:
        host, rest = line.strip().split(' - - ')
        time, http, response = rest.strip().split('"')
        code, size = response.strip().split()
        if int(code) == 404:
            print("{0}\t{1}".format(host, 1))
    except:
        sys.stderr.write("Failed to parse, skipping. Line: {0}\n".format(line.strip()))
