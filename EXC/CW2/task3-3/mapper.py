#!/usr/bin/python3.4
# mapper.py
import sys
from datetime import datetime

# Input:
# in24.inetnebr.com - - [01/Aug/1995:00:00:01 -0400] "GET /shuttle/missions/sts-68/news/sts-68-mcc-05.txt HTTP/1.0" 200 1839
#
# Output:
# in24.inetnebr.com     <unix timestamp>
for line in sys.stdin:
    try:
        host, rest = line.strip().split(' - - ')
        time, _, _ = rest.strip().split('"')
        time = time.replace('[', '').replace(']', '').strip()

        # print(time)
        timestamp = datetime.strptime(time, '%d/%b/%Y:%H:%M:%S %z').timestamp()
        # print(timestamp)
        # Maintain the actual time information as well for output reasons, timestamps lose timezone info
        print("{0} {1} {2}".format(host, int(timestamp), time))

    except:
        sys.stderr.write("Failed to parse, skipping. Line: {0}\n".format(line.strip()))
