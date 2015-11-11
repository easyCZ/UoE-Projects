#!/usr/bin/python
# range.py
import sys
from collections import namedtuple

AccessRange = namedtuple('AccessRange', ['host', 'time_from', 'time_to'])

# Input:
# 1.ts2.mnet.medstroms.se 807657230 05/Aug/1995:17:13:50 -0400
# 1.ts2.mnet.medstroms.se 807657240 05/Aug/1995:17:14:00 -0400
# 1.ts2.mnet.medstroms.se 807657248 05/Aug/1995:17:14:08 -0400
# 1.ts2.mnet.medstroms.se 807657248 05/Aug/1995:17:14:08 -0400
# 11.ts1.mnet.medstroms.se 807637425 05/Aug/1995:11:43:45 -0400
#
# Output:
# 1.ts2.mnet.medstroms.se 807657230 05/Aug/1995:17:13:50 -0400
# 1.ts2.mnet.medstroms.se 807657248 05/Aug/1995:17:14:08 -0400
# 11.ts1.mnet.medstroms.se 807637425 05/Aug/1995:11:43:45 -0400
#
# Approach:
#   * Input is sorted by host and secondarily sorted by UNIX timestamp
#   * Keep track of the first visit and find the last row for the same host
#   * Output the start time and optionally the last time

def write(acs_range):
    # Write <hostname> <timestamp> <datetime>
    print('{0} {1} {2}'.format(acs_range.host, acs_range.time_from[0], acs_range.time_from[1]))
    # Start and end is different
    if acs_range.time_to and acs_range.time_from[0] != acs_range.time_to[0]:
        print('{0} {1} {2}'.format(acs_range.host, acs_range.time_to[0], acs_range.time_to[1]))

access_range = None

for line in sys.stdin:
    host, timestamp, datetime = line.strip().split(' ', 2)

    if not access_range:
        # Initialize
        access_range = AccessRange(host, (timestamp, datetime), None)

    if access_range.host != host:
        write(access_range)

        # New host row
        access_range = AccessRange(host, (timestamp, datetime), None)
    else:
        # The same host again, update end time
        access_range = AccessRange(access_range.host, access_range.time_from, (timestamp, datetime))

# Write the final
if access_range:
    write(access_range)