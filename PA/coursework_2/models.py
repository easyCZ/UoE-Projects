from collections import namedtuple
from enum import Enum, unique
import math


class Stats(object):

    def __init__(self):
        self.hits = 0
        self.misses = 0
        self.invalidated = 0
        self.lines_invalidated = 0


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

    def is_explanation(self):
        return self.command== 'v'

    def is_print(self):
        return self.command == 'p'

    def is_hit(self):
        return self.command == 'h'

    def is_invalidations(self):
        return self.command == 'i'

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

    def __repr__(self):
        return self.name.replace('_', ' ').capitalize()

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

    def get_remotes(self, instruction):
        cpu = instruction.processor_id
        remote_ids = set(self.ids) - set([cpu])
        return [self.caches[cid] for cid in remote_ids]

    def is_shared(self, instruction):
        remotes = self.get_remotes(instruction)
        for cache in remotes:
            try:
                cache.get(instruction)
                return True
            except KeyError:
                pass
        return False

    def invalidate(self, instruction):
        remotes = self.get_remotes(instruction)

        line_invalidates = 0
        for cache in remotes:
            try:
                cache.get(instruction)
                cache.set(instruction, State.invalid)
                line_invalidates += 1
            except KeyError:
                pass

        return line_invalidates

    def message(self, instruction, action):
        remotes = self.get_remotes(instruction)

        lines_invalidated = 0
        old_states = []

        for cache in remotes:
            try:
                entry, state, block = cache.get(instruction)
                new_state = self.protocol.remote(state, action)
                old_states.append((cache.cpu, state))

                if state is not new_state:
                    cache.set(instruction, new_state)

                    if new_state is State.invalid:
                        lines_invalidated += 1
            except KeyError as e:
                # Cache doesn't contain the key, nothing to do
                pass

        if lines_invalidated > 0:
            return (1, lines_invalidated, old_states)
        return (0, lines_invalidated, old_states)


class DirectMappedCache(object):
    """
    Build a direct mapped cache, units are WORDS.
    """
    WORD_SIZE = 4  # 4 bytes = 32 bits
    CACHE_SIZE = 2048   # words
    ADDRESS_LENGTH = WORD_SIZE * 8

    def __init__(self, cpu, block_size=4):
        block_count = self.CACHE_SIZE / block_size

        self.block_size = block_size
        self.block_count = block_count
        self.cpu = cpu

        # Calculate the size of offset, index and tag
        self.offset_length = int(math.log(block_size, 2))
        self.index_length = int(math.ceil(math.log(self.block_count, 2)))
        self.tag_length = self.ADDRESS_LENGTH - self.offset_length - self.index_length

        self.cache = {}

    def __repr__(self):
        return 'CPU {} Cache {}'.format(self.cpu, self.cache)

    def __str__(self):
        header = 'P{}'.format(self.cpu)
        contents = [header]
        for key, value in sorted(self.cache.items()):
            tag, state = value
            contents.append('{}: {} ({})'.format(key, tag, repr(state)))
        return '\n'.join(contents)

    def get_block(self, instruction):
        """
        Map an address to a block.
        """
        mem_block = int(math.floor(instruction.address / self.block_size))
        block = int(mem_block % self.block_count)
        tag = int(math.floor(mem_block / self.block_count))

        return (block, tag)

    def get(self, instruction):
        block, tag = self.get_block(instruction)
        cached_tag, state = self.cache[block]
        if tag != cached_tag or state is State.invalid:
            raise KeyError('Miss on {}'.format(instruction))
        return (tag, state, block)

    def set(self, instruction, state):
        block, tag = self.get_block(instruction)
        self.cache[block] = (tag, state)

        assert len(self.cache) <= self.block_count
