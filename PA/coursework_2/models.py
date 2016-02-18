from collections import namedtuple


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

    def address_to_bin(self):
        """
        Convert an address to binary, left padded to match ADDRESS_LENGTH.
        """
        return bin(self.address)[2:].zfill(self.address_length)

    def get_address_partions(self, tag, index, block):
        assert self.address_length == tag + index + block

        bin_addr = self.address_to_bin()
        return BinaryAddress(
            bin_addr[:tag],
            bin_addr[tag:tag + index],
            bin_addr[tag + index:tag + index + offset])

    def __str__(self):
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
        return len(input) == 1 and input in Command.VERBOSE.keys()
