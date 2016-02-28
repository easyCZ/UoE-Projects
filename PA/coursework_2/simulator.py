#!/usr/local/bin/python3.4
import argparse
import sys
from models import Instruction, Command, DirectMappedCache, State, Action, Bus
from protocols import MSI


class Simulator(object):

    def __init__(self, protocol, processor_count=4):
        self.caches = [DirectMappedCache(i) for i in range(processor_count)]
        self.processor_ids = set(range(processor_count))
        self.protocol = protocol
        self.bus = Bus(self.caches, self.protocol)

    def _partition(self, local_cpu):
        return (local_cpu, set(self.processor_ids) - set([local_cpu]))

    def _partion_caches(self, local_cpu):
        remotes = map(lambda cpu: self.caches[cpu], self._partition(local_cpu))
        return (self.caches[local_cpu], remotes)

    def simulate(self, trace):
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
                except KeyError:
                    # miss
                    state = State.invalid
                    action = Action.translate(instruction.is_read(), False)

                # action = cache.action(instruction)
                new_state = self.protocol.local(state, action)
                print('CPU {}: {} -> {} -> {}'.format(pid, state, action, new_state))

                cache.set(instruction, new_state)

                # update other caches
                self.bus.message(instruction, action)


                # local, remotes = self._partition(pid)
                # print(local, list(remotes))

                # states = list(map(lambda c: c.state(instruction), self.caches))
                # action = cache.action(instruction)

                # new_local_state = self.protocol.local(states[local], action)
                # new_remote_states = []
                # for remote in remotes:
                #     new_remote_states.append(self.protocol.remote(states[remote], action))

                # print(new_local_state, new_remote_states)



                # if instruction.is_read():

                # elif instruction.is_write():

                # else:
                #     print('Invalid instruction {}'.format(instruction), file=sys.stderr)

                # processor_cache.process(instruction)

                # TODO: Process instruction
            elif Command.is_valid(line):
                command = Command(line)
                # TODO: Process instruction
                print(command)
            else:
                print('Error: Failed to parse "{}" on line {}'.format(line, line_number), file=sys.stderr)

        for cache_id, cache in enumerate(self.caches):
            print(cache_id, cache.cache)





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
