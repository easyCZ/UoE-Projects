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
        """
        Convert an address to hexadecimal.
        """
        return int(address, 16)

    def address_to_bin(self, address):
        """
        Convert an address to binary, left padded to match ADDRESS_LENGTH.
        """
        return bin(int(address, 16))[2:].zfill(self.ADDRESS_LENGTH)

    def address_map(self, address, tag_len, index_len, offset_len):
        """
        Split an address in binary into tag, index and offset.
        """
        bin_addr = self.address_to_bin(address)
        assert len(bin_addr) == self.ADDRESS_LENGTH
        return BinaryAddress(
            bin_addr[:tag_len],
            bin_addr[tag_len:tag_len + index_len],
            bin_addr[tag_len + index_len:tag_len + index_len + offset_len])

    def parse(self, instruction):
        """
        Extract action and address from a line of the file.
        """
        return Instruction(*instruction.split())

    def get_block(self, address, cache_blocks_len):
        """
        Map an address to a block.
        """
        return int(address, 2) % cache_blocks_len

    def display_stats(self, stats):
        """
        Display statistics about the simulation.
        """
        total = stats['r_miss'] + stats['w_miss'] + stats['r_hit'] + stats['w_hit']
        total_miss_rate = 100 * (stats['r_miss'] + stats['w_miss']) / float(total)
        read_miss_rate = 100 * stats['r_miss'] / float(stats['r_miss'] + stats['r_hit'])
        write_miss_rate = 100 * stats['w_miss'] / float(stats['w_miss'] + stats['w_hit'])

        print 'Total miss rate: %.6f' % total_miss_rate
        print 'Read miss rate: %.6f' % read_miss_rate
        print 'Write miss rate: %.6f' % write_miss_rate


class DirectMapped(Cache):
    """
    Direct mapped cache is the same as Set associative where the associativity is 1.
    Using the SetAssociative implementation instead.
    """
    pass


class SetAssociative(Cache):

    def __init__(self, filename, cache_size, associativity):
        self.filename = filename
        self.size = 1024 * cache_size     # Size in bytes
        self.cache_blocks = self.size / self.BLOCK_SIZE
        self.cache_sets = self.cache_blocks / associativity

        self.associativity = associativity

        # Calculate the size of offset, index and tag
        self.offset_length = int(math.log(self.BLOCK_SIZE, 2))
        self.index_length = int(math.ceil(math.log(self.cache_sets, 2)))
        self.tag_length = self.ADDRESS_LENGTH - self.offset_length - self.index_length

        # Use deque for each set, automatically losing elements as more data is appended
        self.cache = [deque(maxlen=associativity) for i in range(self.cache_sets)]

    def read_write(self, bin_address, op, stats):
        """
        Perform READ/WRITE operation. Updates the cache.
        """
        hit, miss = ('r_hit', 'r_miss') if op == self.READ else ('w_hit', 'w_miss')
        cache_block = self.get_block(bin_address.index, self.cache_sets)

        # Remove the entry if exists
        try:
            self.cache[cache_block].remove(bin_address.tag)
            stats[hit] += 1
        except ValueError:
            stats[miss] += 1

        self.cache[cache_block].append(bin_address.tag)

    def simulate(self):
        """
        Main entry of the simulation.
        """
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


if __name__ == '__main__':
    valid_models = ['set_associative', 'direct_mapped']

    parser = argparse.ArgumentParser()
    parser.add_argument('model', help='Which cache model to use.', type=str, choices=valid_models)
    parser.add_argument('filename', help="The file to be used for simulation.", type=str)
    parser.add_argument('cache_size', help="Size of the cache in KB", type=int)
    parser.add_argument('-set_size', help="The size of the set", type=int)

    args = parser.parse_args()

    if args.set_size is None:
        args.sets = 1

    try:
        if args.model in valid_models:
            model = args.model
            if model == 'direct_mapped':
                c = SetAssociative(args.filename, int(args.cache_size), 1)
            if model == 'set_associative':
                c = SetAssociative(args.filename, int(args.cache_size), int(args.set_size))
            c.simulate()
        else:
            print 'Valid options for cache model are', valid_models
    except ValueError, e:
        print 'cache_size and set_size need to be integers.'
