#!/usr/bin/python2.7
# mapper.py
import sys
from datetime import datetime


def parse_datetime(dt):
    timestamp = dt.split()[0]
    return datetime.strptime(timestamp, '%d/%b/%Y:%H:%M:%S')


def get_timestamp(dt):
    return (dt - datetime(1970, 1, 1)).total_seconds()

# Input:
# in24.inetnebr.com - - [01/Aug/1995:00:00:01 -0400] "GET /shuttle/missions/sts-68/news/sts-68-mcc-05.txt HTTP/1.0" 200 1839
#
# Output:
# in24.inetnebr.com     <unix timestamp>
for line in sys.stdin:
    try:
        host, rest = line.strip().split(' - - ')
        time, _ = rest.strip().split('"', 1)
        time = time.replace('[', '').replace(']', '').strip()
        timestamp = get_timestamp(parse_datetime(time))

        # Maintain the actual time information as well for output reasons, timestamps lose timezone info
        print("{0} {1} {2}".format(host, int(timestamp), time))

    except:
        sys.stderr.write("Failed to parse, skipping. Line: {0}\n".format(line.strip()))
