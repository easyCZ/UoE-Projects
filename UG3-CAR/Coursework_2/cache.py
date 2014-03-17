import argparse
import math
from collections import namedtuple, deque

BinaryAddress = namedtuple('BinaryAddress', 'tag index offset')
Instruction = namedtuple('Instruction', 'op address')
CacheEntry = namedtuple('CacheEntry', 'tag data')


class Cache(object):

    ADDRESS_LENGTH = 48     # 48 bits for an address
    BLOCK_SIZE = 32         # 32 bits for each cache line
    READ, WRITE = 'R', 'W'

    def address_to_int(self, address):
        return int(address, 16)

    def address_to_bin(self, address):
        return bin(int(address, 16))[2:].zfill(self.ADDRESS_LENGTH)

    def address_map(self, address, tag_len, index_len, offset_len):
        bin_addr = self.address_to_bin(address)
        assert len(bin_addr) == self.ADDRESS_LENGTH
        return BinaryAddress(
            bin_addr[:tag_len],
            bin_addr[tag_len:tag_len + index_len],
            bin_addr[tag_len + index_len:tag_len + index_len + offset_len])

    def parse(self, instruction):
        return Instruction(*instruction.split())

    def get_block(self, address, cache_blocks_len):
        return int(address) % cache_blocks_len

    def display_stats(self, stats):
        total = stats['r_miss'] + stats['w_miss'] + stats['r_hit'] + stats['w_hit']
        total_miss_rate = 100 * (stats['r_miss'] + stats['w_miss']) / float(total)
        read_miss_rate = 100 * stats['r_miss'] / float(stats['r_miss'] + stats['r_hit'])
        write_miss_rate = 100 * stats['w_miss'] / float(stats['w_miss'] + stats['w_hit'])

        print 'Total miss rate: %.6f' % total_miss_rate
        print 'Read miss rate: %.6f' % read_miss_rate
        print 'Write miss rate: %.6f' % write_miss_rate


class DirectMapped(Cache):

    def __init__(self, filename, cache_size):
        self.filename = filename
        self.size = 1024 * cache_size
        self.cache_blocks = self.size / self.BLOCK_SIZE
        self.offset_length = int(math.log(self.BLOCK_SIZE, 2))
        self.index_length = int(math.log(self.cache_blocks, 2))
        self.tag_length = self.ADDRESS_LENGTH - self.offset_length - self.index_length

        self.cache = [None] * self.cache_blocks

    def write(self, bin_address, stats):
        cache_pos = self.get_block(bin_address.index, self.cache_blocks)
        if self.cache[cache_pos] is not None and self.cache[cache_pos].tag == bin_address.tag:
                stats['w_hit'] += 1
        else:
            stats['w_miss'] += 1
        self.cache[cache_pos] = CacheEntry(bin_address.tag, None)

    def read(self, bin_address, stats):
        cache_pos = self.get_block(bin_address.index, self.cache_blocks)
        if self.cache[cache_pos] is not None and self.cache[cache_pos].tag == bin_address.tag:
                stats['r_hit'] += 1
        else:
            stats['r_miss'] += 1
        self.cache[cache_pos] = CacheEntry(bin_address.tag, None)

    def simulate(self):
        stats = {'r_miss': 0, 'w_miss': 0, 'r_hit': 0, 'w_hit': 0}
        try:
            with open(self.filename) as _file:
                for line in _file:
                    instruction = self.parse(line)
                    bin_addr = self.address_map(
                        instruction.address,
                        self.tag_length, self.index_length, self.offset_length)

                    if instruction.op == self.WRITE:
                        self.write(bin_addr, stats)
                    else:
                        self.read(bin_addr, stats)

            self.display_stats(stats)

        except IOError:
            print("File '%s' could not be found." % self.filename)


class SetAssociative(Cache):

    def __init__(self, filename, cache_size, associativity):
        self.filename = filename
        self.size = 1024 * cache_size     # Size in bytes
        self.cache_blocks = self.size / self.BLOCK_SIZE
        self.cache_sets = self.cache_blocks / associativity
        self.associativity = associativity
        self.offset_length = int(math.log(self.BLOCK_SIZE, 2))
        self.index_length = int(math.floor(math.log(self.cache_blocks, 2)))
        self.tag_length = self.ADDRESS_LENGTH - self.offset_length - self.index_length

        self.cache = [deque(maxlen=associativity)] * self.cache_sets

        print('Cache size: %d B' % self.size)
        print('Cache blocks: %d' % self.cache_blocks)
        print('Cache sets: %d' % self.cache_sets)
        print('Tag: %d bits, Index: %d bits, Offset: %d bits' % (self.tag_length, self.index_length, self.offset_length))

    def read_write(self, bin_address, op, stats):
        hit, miss = ('r_hit', 'r_miss') if op == self.READ else ('w_hit', 'w_miss')
        cache_block = self.get_block(bin_address.index, self.cache_sets)
        # Remove the entry if exists
        try:
            self.cache[cache_block].remove(CacheEntry(bin_address.tag, None))
            stats[hit] += 1
        except ValueError:
            stats[miss] += 1

        self.cache[cache_block].appendleft(CacheEntry(bin_address.tag, None))

    def simulate(self):
        stats = {'r_miss': 0, 'w_miss': 0, 'r_hit': 0, 'w_hit': 0}
        try:
            with open(self.filename) as _file:
                for line in _file:
                    instruction = self.parse(line)
                    bin_addr = self.address_map(
                        instruction.address,
                        self.tag_length, self.index_length, self.offset_length)

                    self.read_write(bin_addr, instruction.op, stats)

            self.display_stats(stats)

        except IOError:
            print("File '%s' could not be found." % self.filename)







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
#
c = SetAssociative('gcc_memref.out', 8, 2)
c.simulate()
#
# d = DirectMapped('gcc_memref.out', 16)
# d.simulate()
# print(c.cache_blocks)
# print c.address_map('1682318', 36, 7, 5)
