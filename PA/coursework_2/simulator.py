#!/usr/local/bin/python3.4
import argparse
import sys


class Instruction(object):

    ACTIONS = ['R', 'W']
    ACTION_READ = 'read'
    ACTION_WRITE = 'write'
    ACTIONS_VERBOSE = {'R': ACTION_READ, 'W': ACTION_WRITE}

    def __init__(self, instruction):
        self.instruction = instruction
        processor, action, address = instruction.split()
        self.processor = processor
        self.processor_id = int(processor[1])
        self.action = action
        self.address = int(address)

    def __str__(self):
        return self.instruction

    def explain(self):
        return 'a {0} by processor {1} to word {2}'.format(
            self.ACTIONS_VERBOSE[self.action],
            self.processor_id,
            self.address
        )

class Simulator(object):

    def simulate(self, trace):
        for line in trace:
            instruction = Instruction(line)
            print(instruction.explain())


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
