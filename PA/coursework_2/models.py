from collections import namedtuple
from enum import Enum, unique
import sys
import math


BinaryAddress = namedtuple('BinaryAddress', 'tag index offset')


class Instruction(object):

    ACTIONS = ['R', 'W']

    def __init__(self, instruction, address_length=32):
        self.instruction = instruction
        processor, action, address = instruction.split()
        self.processor = processor
        self.processor_id = int(processor[1])
        self.action = action
        self.address = int(address)
        self.address_length = address_length

    def is_read(self):
        return self.action == 'R'

    def is_write(self):
        return self.action == 'W'

    def address_to_bin(self):
        """
        Convert an address to binary, left padded to match ADDRESS_LENGTH.
        """
        return bin(self.address)[2:].zfill(self.address_length)

    def get_address_partions(self, tag, index, block):
        """
        Break an instruction address into a tag, index and a block
        """
        assert self.address_length == tag + index + block

        bin_addr = self.address_to_bin()
        return BinaryAddress(
            bin_addr[:tag],
            bin_addr[tag:tag + index],
            bin_addr[tag + index:tag + index + offset])

    def __str__(self):
        return self.instruction

    def __repr__(self):
        return self.instruction

    @staticmethod
    def is_valid(input):
        """
        It is an instruction if we can break it down to 3 pieces
        """
        return len(input.split()) == 3


class Command(object):

    VERBOSE = {
        'v': 'Toggle full line by line explanation (verbose)',
        'p': 'Print contents of the cache',
        'h': 'Print the current hit-rate',
        'i': 'Print the total number of invalidations'
    }

    def __init__(self, command):
        self.command = command
        self.verbose = self.VERBOSE[command]

    def __str__(self):
        return 'Instruction({}) - {}'.format(self.command, self.verbose)

    @staticmethod
    def is_valid(input):
        """
        Check a plaintext input and decide if it is a valid command
        """
        return len(input) == 1 and input in Command.VERBOSE.keys()


@unique
class State(Enum):
    modified = 'M'
    shared = 'S'
    invalid = 'I'
    exclusive = 'E'

    def __repr__(self):
        return self.name.capitalize()


@unique
class Action(Enum):
    read_hit = 'RH'
    read_miss = 'RM'
    write_hit = 'WH'
    write_miss = 'WM'

    @staticmethod
    def translate(read, hit):
        if not read and not hit:
            return Action.write_miss
        if not read and hit:
            return Action.write_hit
        if read and not hit:
            return Action.read_miss
        return Action.read_hit


class Bus(object):

    def __init__(self, caches, protocol):
        self.caches = caches
        self.protocol = protocol
        self.ids = range(len(caches))

    def message(self, instruction, action):
        cpu = instruction.processor_id
        remote_ids = set(self.ids) - set([cpu])
        remotes = [self.caches[cid] for cid in remote_ids]

        for cache in remotes:
            print(cache.cache, action)
            try:
                entry, state = cache.get(instruction)
                new_state = self.protocol.remote(state, action)
                cache.set(instruction, new_state)
            except KeyError as e:
                print(e)



class Cache(object):

    def get_block(self, address, cache_blocks_len):
        """
        Map an address to a block.
        """
        return address % cache_blocks_len


class DirectMappedCache(Cache):
    """
    Build a direct mapped cache, units are WORDS.
    """
    WORD_SIZE = 4  # 4 bytes = 32 bits
    CACHE_SIZE = 2048   # words
    ADDRESS_LENGTH = WORD_SIZE * 8

    def __init__(self, cpu, block_size=4, block_count=512):
        assert block_size * block_count == self.CACHE_SIZE

        self.block_size = block_size
        self.block_count = block_count
        self.cpu = cpu

        # Calculate the size of offset, index and tag
        self.offset_length = int(math.log(block_size, 2))
        self.index_length = int(math.ceil(math.log(self.block_count, 2)))
        self.tag_length = self.ADDRESS_LENGTH - self.offset_length - self.index_length

        self.cache = {}

    def __hash__(self):
        return hash(self.cpu)

    def __repr__(self):
        return 'CPU {} Cache {}'.format(self.cpu, super(DirectMappedCache, self).__repr__())

    def get(self, instruction):
        block = self.get_block(instruction.address, self.block_count)
        cached, state = self.cache[block]
        if cached.instruction == instruction.instruction:
            return (cached, state)
        raise KeyError('Miss on {}'.format(instruction))

    def set(self, instruction, state):
        block = self.get_block(instruction.address, self.block_count)
        self.cache[block] = (instruction, state)

        assert len(self.cache) <= self.block_count





    # def state(self, instruction):
    #     block = self.get_block(instruction.address, self.block_count)
    #     if block in self.cache:
    #         return self.get(block)[1]
    #     return State.invalid

    # def action(self, instruction):
    #     block = self.get_block(instruction.address, self.block_count)
    #     if block in self:
    #         return Action.read_hit if instruction.is_read() else Action.write_hit
    #     return Action.read_miss if instruction.is_read() else Action.write_miss

    # def process(self, instruction):
    #     if instruction.is_read():
    #         self.read(instruction)
    #     elif instruction.is_write():
    #         self.write(instruction)
    #     else:
    #         print('Invalid instruction {}'.format(instruction), file=sys.stderr)
    #         return

    # def read(self, instruction):
    #     block = self.get_block(instruction.address, self.block_count)
    #     try:
    #         # TODO: Check tag
    #         return (Action.read_hit, self.get(block))
    #     except KeyError:
    #         self[block] = (instruction,

    # def write(self, instruction):
    #     block = self.get_block(instruction.address, self.block_count)
    #     self[block] = instruction

    #     assert len(self) <= self.block_count
