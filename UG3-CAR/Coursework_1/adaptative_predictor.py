#   Computer Architecture 2014 - University of Edinburgh
#   Student ID: s1115104

import math


class AdaptativePredictor(object):
    """
    Adaptative 2-bit Branch Predictor based on n-bit history.
    """

    def tokenize(self, line):
        """
        Split a line into tokens, return a dictionary with address and the outcome.
        """
        tokens = line.split()
        return {'address': tokens[1], 'taken': int(tokens[2])}

    def init_history(self, length):
        """
        Initialize history of length 'length'.
        """
        return [0] * length

    def init_predictor(self):
        """
        Initialize to weakly not taken.
        """
        return 1

    def to_bit_string(self, bits):
        """
        Return bit string of the given bits represented as a list.
        """
        return ''.join(map(str, bits))

    def get_history(self, address, table):
        """
        Get branch history for a given address.

        Returns a list of outcomes
        """
        return table[address]['history']

    def predictor(self, instruction, history_table, length):
        """
        Predict whether the branch should be taken or not based on its history.
        If instruction has no history, initialize history to all zeroes.

        Params:
            [dict]  instruction     instruction information
            [table] history_table   dictionary of histories, keys are dictionaries
                                    themselves with state for each history
            [int]   length          Length of the history to keep for initialization purposes

        Returns:
            0 or 1 based on whether we predict to take the branch or not.
        """
        address = instruction['address']
        if address in history_table.keys():
            if 'history' in history_table[address]:
                history = history_table[address]['history']
                history_bitstring = self.to_bit_string(history)

                if history_bitstring in history_table[address]:
                    if history_table[address][history_bitstring] >= 2:
                        return 1
                    else:
                        return 0

                else:
                    history_table[address][history_bitstring] = self.init_predictor()
                    return 0

            else:
                print "Could not find 'history' key in the history table."
        else:
            history = self.init_history(length)
            history_key = self.to_bit_string(history)
            history_table[address] = dict()
            history_table[address]['history'] = history
            history_table[address][history_key] = self.init_predictor()
            return self.predictor(instruction, history_table, length)

    def compare(self, instruction, prediction):
        """
        Compare the prediction of a branch with the actual outcome.
        """
        return instruction['taken'] == prediction

    def udpate_history(self, instruction, history_table, outcome, length):
        address = instruction['address']

        old_history = history_table[address]['history']

        # Push new elements onto the history and remove the tail - Should really use a Queue here.
        history_table[address]['history'].insert(0, outcome)
        history_table[address]['history'] = history_table[address]['history'][:length]
        new_history = history_table[address]['history']
        history_table[address]['history'] = new_history

    def update_state(self, instruction, history_table, outcome, length):
        address = instruction['address']
        history = history_table[address]['history']
        history_bitstring = self.to_bit_string(history)


        # print 'history_table', history_table
        # print 'address:', address
        # print 'history_bitstring:', history_bitstring

        if outcome == 1:    # increment
            history_table[address][history_bitstring] = min(
                history_table[address][history_bitstring] + 1, length -1)   # Make sure we stay within the length
        else:   # decrement
            # print 'history_table[address][history_bitstring]', history_table[address][history_bitstring]
            history_table[address][history_bitstring] = max(
                history_table[address][history_bitstring] - 1, 0)   # Make sure we don't go below 0

    def analyze(self, filename, length):
        table = dict()
        stats = {'hit': 0, 'miss': 0}
        with open(filename) as _file:
            for line in _file:
                instruction = self.tokenize(line)

                prediction = self.predictor(instruction, table, length)

                # Update stats counter
                if self.compare(instruction, prediction):
                    stats['hit'] += 1
                else:
                    stats['miss'] += 1

                self.update_state(instruction, table, instruction['taken'], length)
                self.udpate_history(instruction, table, instruction['taken'], length)


        self.table = table

        print 'Miss rate: %.3f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')


a = AdaptativePredictor()
a.analyze('gcc_branch.out', 1)

