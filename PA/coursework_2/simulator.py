#!/usr/local/bin/python3.4
import argparse
import sys
from models import Instruction, Command, DirectMappedCache, State, Action


class Simulator(object):

    def __init__(self, processor_count=4):
        self.caches = {}

        for processor in range(processor_count):
            self.caches[processor] = DirectMappedCache()

    def simulate(self, trace):
        for line_number, line in enumerate(trace):
            line = line.strip()

            if Instruction.is_valid(line):
                instruction = Instruction(line)

                processor_cache = self.caches[instruction.processor_id]

                processor_cache.process(instruction)

                # TODO: Process instruction
            elif Command.is_valid(line):
                command = Command(line)
                # TODO: Process instruction
                print(command)
            else:
                print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)

        for cache_id, cache in self.caches.items():
            print(cache_id, str(cache))





def main():
    args = parse_args()
    print('Arguments used: {}'.format(vars(args)))

    source = open(args.file) if args.file else sys.stdin

    # Run simulator
    simulator = Simulator()
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
