#!/usr/local/bin/python3.4
import argparse
import sys

from models import Instruction, Command, DirectMappedCache, State, Action, Bus
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

    def simulate(self, trace):
        print(self.info())

        stats = {
            'hit': 0,
            'miss': 0,
            'invalidations': [],
            'updates': 0
        }
        instruction_number = 0
        for line_number, line in enumerate(trace):
            line = line.strip()

            if Instruction.is_valid(line):
                instruction_number += 1
                instruction = Instruction(line)
                pid = instruction.processor_id
                cache = self.caches[pid]

                try:
                    # hit
                    tag, state, block = cache.get(instruction)
                    action = Action.translate(instruction.is_read(), True)
                    stats['hit'] += 1
                except KeyError:
                    # miss
                    state = State.invalid
                    action = Action.translate(instruction.is_read(), False)
                    block, tag = cache.get_block(instruction)
                    stats['miss'] += 1

                # Find State transition
                new_state = self.protocol.local(state, action)
                cache.set(instruction, new_state)

                # update other caches
                invalidations, lines_invalidated, old_states = self.bus.message(instruction, action)

                if invalidations > 0:
                    stats['invalidations'].append(lines_invalidated)

                if self.verbose:
                    message = 'A {} by {} to word {} looked for tag {} in block {}, found in state {}.'.format(
                        repr(action), 'P{}'.format(instruction.processor_id), instruction.address, tag, block, repr(state)
                    )
                    if len(old_states) > 0:
                        message += ' Other CPUs are in states {}'.format(
                            str(list(map(lambda x: 'P{} - {}'.format(x[0], repr(x[1])), old_states)))
                        )
                    else:
                        message += ' Other CPUs do not contain copies of this address'
                    print(message)

            elif Command.is_valid(line):
                command = Command(line)

                if command.is_explanation():
                    self.verbose = not self.verbose

                elif command.is_hit():
                    # print current hit rate
                    hits = float(stats['hit'])
                    totals = float(stats['hit'] + stats['miss'])
                    print('Hit Rate: {}. IC: {}'.format(
                        hits / totals, instruction_number
                    ))

                elif command.is_invalidations():
                    # print the current number of invalidations
                    invalidations = sum(stats['invalidations'])
                    print('# of invalidations: {}. IC: {}'.format(
                        invalidations, instruction_number
                    ))

                elif command.is_print():
                    output = '\n'.join(map(str, self.caches))
                    print(output)

            else:
                print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)


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
