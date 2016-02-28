#!/usr/local/bin/python3.4
import argparse
import sys
from collections import defaultdict

from models import Instruction, Command, DirectMappedCache, State, Action, Bus
from protocols import MSI


class Simulator(object):

    def __init__(self, protocol, processor_count=4):
        self.caches = [DirectMappedCache(i) for i in range(processor_count)]
        self.processor_ids = set(range(processor_count))
        self.protocol = protocol
        self.bus = Bus(self.caches, self.protocol)
        self.verbose = False

    def simulate(self, trace):
        stats = {
            'hit': 0,
            'miss': 0,
            'invalidations': [],
            'updates': 0
        }
        for line_number, line in enumerate(trace):
            line = line.strip()

            if Instruction.is_valid(line):
                instruction = Instruction(line)
                pid = instruction.processor_id
                cache = self.caches[pid]

                try:
                    # hit
                    inst, state = cache.get(instruction)
                    action = Action.translate(instruction.is_read(), True)
                    stats['hit'] += 1
                except KeyError:
                    # miss
                    state = State.invalid
                    action = Action.translate(instruction.is_read(), False)
                    stats['miss'] += 1

                # Find State transition
                new_state = self.protocol.local(state, action)
                cache.set(instruction, new_state)

                # update other caches
                lines_invalidated = self.bus.message(instruction, action)
                invalidations = 0
                if action is Action.write_miss:
                    invalidations = 1

                if invalidations > 0:
                    stats['invalidations'].append(lines_invalidated)

                if self.verbose:
                    pass
                    # TODO: Print human readable actions

            elif Command.is_valid(line):
                command = Command(line)

                if command.is_explanation():
                    self.verbose = not self.verbose

                elif command.is_hit():
                    pass

                elif command.is_invalidations():
                    pass

                elif command.is_print():
                    pass

            else:
                print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)

        for cache_id, cache in enumerate(self.caches):
            print(cache_id, cache.cache)

        print('Hit rate', float(stats['hit']) / float(stats['hit'] + stats['miss']))
        print('Invalidations:', sum(stats['invalidations']))





def main():
    args = parse_args()
    print('Arguments used: {}'.format(vars(args)))

    source = open(args.file) if args.file else sys.stdin

    # Run simulator
    protocol = MSI()
    simulator = Simulator(protocol)
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
    return parser.parse_args()

if __name__ == '__main__':
    main()
