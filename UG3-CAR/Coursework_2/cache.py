import argparse
import math
from collections import namedtuple

Address = namedtuple('Address', 'tag index offset')


class Cache(object):

    ADDRESS_LENGTH = 48     # 48 bits for an address

    def address_to_int(self, address):
        return int(address, 16)

    def address_to_bin(self, address):
        return bin(int(address, 16))[2:].zfill(self.ADDRESS_LENGTH)

    def address_map(self, address, tag_len, index_len, offset_len):
        bin_addr = self.address_to_bin(address)
        print bin_addr
        return Address(
            bin_addr[:tag_len],
            bin_addr[tag_len:tag_len + index_len],
            bin_addr[tag_len + index_len:tag_len + index_len + offset_len])


class DirectMapped(Cache):

    BLOCK_SIZE = 32

    def __init__(self, filename, cache_size):
        self.size = 1024 * cache_size
        self.offset_length = int(math.log(BLOCK_SIZE, 2))
        self.index_length = int(math.log(self.size / BLOCK_SIZE), 2)
        self.tag_length = BLOCK_SIZE - self.offset_length - self.index_length

    def get_block(self, address, cache_size):
        return address % cache_size


# if __name__ == "__main__":
#     parser = argparse.ArgumentParser()
#     parser.add_argument('filename', help='File to analyze by the predictor.')
#     parser.add_argument('cache_size', help='Size of the cache to model.')

#     args = parser.parse_args()
#     try:
#         cache_size = int(args.cache_size)
#         # a = AdaptativePredictor()
#         # a.analyze(args.filename, length)
#     except ValueError, e:
#         print 'Cache Size needs to be an integer.'

c = Cache()
print c.address_map('1682318', 20, 7, 5)
