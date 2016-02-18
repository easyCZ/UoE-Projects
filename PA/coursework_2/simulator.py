#!/usr/local/bin/python3.4
import argparse
import sys
from models import Instruction, Command


class Simulator(object):

    def simulate(self, trace):
        for line_number, line in enumerate(trace):
            line = line.strip()

            if Instruction.is_valid(line):
                instruction = Instruction(line)
                # TODO: Process instruction
                print(instruction)
            elif Command.is_valid(line):
                command = Command(line)
                # TODO: Process instruction
                print(command)
            else:
                print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)





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
