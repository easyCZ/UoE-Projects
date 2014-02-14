#   Computer Architecture 2014 - University of Edinburgh
#   Student ID: s1115104
import math
import argparse


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

        if address in history_table:
            history = self.get_history(address, history_table)
            history_bitstring = self.to_bit_string(history)

            # Ensure we have any history recorded for this sequence
            if history_bitstring not in history_table[address]:
                history_table[address][history_bitstring] = self.init_predictor()

            return 1 if history_table[address][history_bitstring] >= 2 else 0

        else:
            history = self.init_history(length)
            history_key = self.to_bit_string(history)
            history_table[address] = dict()
            history_table[address]['history'] = history
            history_table[address][history_key] = self.init_predictor()
            return 0

    def compare(self, instruction, prediction):
        """
        Compare the prediction of a branch with the actual outcome.
        """
        return instruction['taken'] == prediction

    def udpate_history(self, instruction, history_table, outcome, length):
        address = instruction['address']

        # old_history = history_table[address]['history']

        # Push new elements onto the history and remove the tail - Should really use a Queue here.
        history_table[address]['history'].insert(0, outcome)
        history_table[address]['history'] = history_table[address]['history'][:length]

    def update_state(self, instruction, history_table, outcome, length):
        address = instruction['address']
        history = history_table[address]['history']
        history_bitstring = self.to_bit_string(history)

        if outcome == 1:    # increment
            history_table[address][history_bitstring] = min(
                history_table[address][history_bitstring] + 1, 3)   # Make sure we stay within the length
        else:   # decrement
            # print 'history_table[address][history_bitstring]', history_table[address][history_bitstring]
            history_table[address][history_bitstring] = max(
                history_table[address][history_bitstring] - 1, 0)   # Make sure we don't go below 0

    def analyze(self, filename, length):

        print '## Adaptative Predictor with history length of %d' % (length)
        print '## Analyzing file: %s' % (filename)
        print '#######################################'

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

        print 'Miss rate: %.6f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('filename', help='File to analyze by the predictor.')
    parser.add_argument('history_length', help='Length of the history to keep for the 2 bit saturating predictor.')

    args = parser.parse_args()
    try:
        length = int(args.history_length)
        a = AdaptativePredictor()
        a.analyze(args.filename, length)
    except ValueError, e:
        print 'History Length needs to be an integer between 1 and 4 inclusive'
