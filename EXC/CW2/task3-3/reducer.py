#!/usr/bin/python
# range.py
import sys
from collections import namedtuple

AccessRange = namedtuple('AccessRange', ['host', 'time_from', 'time_to'])

# Input:
# 1.ts2.mnet.medstroms.se 807657230 05/Aug/1995:17:13:50 -0400
# 1.ts2.mnet.medstroms.se 807657248 05/Aug/1995:17:14:08 -0400
# 11.ts1.mnet.medstroms.se 807637425 05/Aug/1995:11:43:45 -0400
#
# Output:
# <hostname>    <seconds from first to last visit>  [<first_visit>]   [<last_visit>]
# Or when only a single visit:
# <hostname>    0  [<first_visit>]   [<first_visit>]
#
# Ie:
# 1.ts2.mnet.medstroms.se 18 [05/Aug/1995:17:13:50 -0400] [05/Aug/1995:17:14:08 -0400]
# 11.ts1.mnet.medstroms.se 0 [05/Aug/1995:11:43:45 -0400] [05/Aug/1995:11:43:45 -0400]
#
# Approach:
#   * Input is sorted by host and secondarily sorted by UNIX timestamp
#   * Keep track of the first visit and find the last row for the same host
#
# Notes:
#   * The amount of data output is slightly more than just the time difference
#     but knowing the host and the length of the stay as well as the timestamps
#     is more useful and better parseable

def write(acs_range):
    delta = 0
    datetime_from = acs_range.time_from[1]
    datetime_to = acs_range.time_from[1]

    if acs_range.time_to:
        delta = int(abs(int(acs_range.time_to[0]) - int(acs_range.time_from[0])))
        datetime_to = acs_range.time_to[1]

    print('{0}\t{1}\t{2}\t{3}'.format(acs_range.host, delta, datetime_from, datetime_to))


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