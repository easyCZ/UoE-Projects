#!/usr/bin/python3.4
import argparse
import sys

from models import Instruction, Command, DirectMappedCache, State, Action, Bus, Stats
from protocols import MSI, MESI, MES


class Simulator(object):

    def __init__(self, protocol, block_size=4, processor_count=4):
        self.block_size = block_size
        self.caches = [DirectMappedCache(i, block_size) for i in range(processor_count)]
        self.processor_ids = set(range(processor_count))
        self.protocol = protocol
        self.bus = Bus(self.caches, self.protocol)
        self.verbose = False

    def info(self):
        return 'Running {} cache coherence simulator on Direct Mapped cache with block size of {} words on {} CPUs'.format(self.protocol, self.block_size, len(self.caches))


    def instruction(self, input, stats):
        instruction = Instruction(input)
        cache = self.caches[instruction.processor_id]

        try:
            # Attempt to hit the cache
            tag, state, block = cache.get(instruction)
            action = Action.translate(instruction.is_read(), True)

            # Check if we need to invalidate all other entries
            if self.protocol.should_invalidate_others(state, action):
                stats.lines_invalidated += self.bus.invalidate(instruction)
                stats.invalidated += 1
                stats.misses += 1
            else:
                stats.hits += 1

                # count private/shared hits
                if state is State.modified or state is State.exclusive:
                    stats.private_hits += 1
                elif state is State.shared:
                    stats.shared_hits += 1

        except KeyError:
            # miss
            state = State.invalid
            action = Action.translate(instruction.is_read(), False)
            block, tag = cache.get_block(instruction)
            stats.misses += 1

        # Determine how to transition next
        is_shared = self.bus.is_shared(instruction)
        new_state = self.protocol.local(state, action, **{'shared': is_shared})

        # Update the state
        cache.set(instruction, new_state)

        # Update all the other caches by passing a message on the bus
        remote_states, invalidates, write_backs = self.bus.message(instruction, action)
        stats.write_backs += write_backs
        if invalidates > 0:
            stats.invalidated += 1
        stats.lines_invalidated += invalidates

        # Print
        if self.verbose:
            message = 'A {} by {} to word {} looked for tag {} in block {}, found in state {}.'.format(
                repr(action), 'P{}'.format(instruction.processor_id), instruction.address, tag, block, repr(state)
            )
            if len(remote_states) > 0:
                message += ' Other CPUs are in states {}'.format(
                    str(list(map(lambda x: 'P{} - {}'.format(x[0], repr(x[1])), remote_states)))
                )
            else:
                message += ' Other CPUs do not contain copies of this address'
            print(message)


    def command(self, input, stats, instruction_number):
        command = Command(input)

        if command.is_explanation():
            self.verbose = not self.verbose
            print('Verbose switched to: {}'.format(self.verbose))

        elif command.is_hit():
            # print current hit rate
            print('Hit Rate: {0:.2f}%. Private hits: {1:.2f}%. Shared hits: {2:.2f}%'.format(
                stats.hit_rate(),
                stats.private_hit_rate(),
                stats.shared_hit_rate()
            ))

        elif command.is_invalidations():
            print('Invalidation broadcasts: {}. Lines invalidated: {}'.format(
                stats.invalidated,
                stats.lines_invalidated
            ))

        elif command.is_print():
            output = '\n'.join(map(str, self.caches))
            print(output)

    def simulate(self, trace):
        print(self.info())

        stats = Stats()

        instruction_number = 0
        for line_number, line in enumerate(trace):
            line = line.strip()

            # skip full line comments and blank lines
            if not line.startswith('#') and line:
                if Instruction.is_valid(line):
                    instruction_number += 1
                    self.instruction(line, stats)
                elif Command.is_valid(line):
                    self.command(line, stats, instruction_number)
                else:
                    print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)

        print(stats)


def main():
    args = parse_args()
    print('Arguments used: {}'.format(vars(args)))

    source = open(args.file) if args.file else sys.stdin

    # Run simulator
    protocols = {
        'msi': MSI(),
        'mesi': MESI(),
        'mes': MES()
    }
    protocol = protocols[args.protocol]
    simulator = Simulator(protocol, args.block_size)
    simulator.simulate(source)

    # Clean up source
    if args.file:
        source.close()

def parse_args():
    parser = argparse.ArgumentParser(description='Cache protocol simulator')
    parser.add_argument('-f',
        dest='file',
        type=str,
        help='Path to the file to use as simulator source.')
    parser.add_argument('-p',
        dest='protocol',
        choices=['msi', 'mesi', 'mes'],
        type=str,
        required=True,
        help='The protocol to use')
    parser.add_argument('-b',
        dest='block_size',
        choices=[2, 4, 8, 16],
        default=4,
        type=int,
        help='The block size to use')
    return parser.parse_args()

if __name__ == '__main__':
    main()
