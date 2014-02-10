import math


class AdaptativePredictor(object):

    def tokenize(self, line):
        tokens = line.split()
        return {'address': tokens[1], 'taken': int(tokens[2])}

    def predictor(self, instruction, table, bits):
        if table[instruction['address']] > math.pow(2, bits - 1):
            return 1
        return 0

    def compare(self, instruction, prediction):
        return instruction['taken'] == prediction

    def increment(self, key, table, bits):
        if table[key] < math.pow(2, bits) - 1:
            table[key] += 1

    def decrement(self, key, table, bits):
        if table[key] > 0:
            table[key] -= 1

    def analyze(self, filename, bits):
        table = dict()
        stats = {'hit': 0, 'miss': 0}
        with open(filename) as _file:
            for line in _file:
                instruction = self.tokenize(line)

                if instruction['address'] not in table.keys():
                    table[instruction['address']] = 0

                prediction = self.predictor(instruction, table, bits)

                if self.compare(instruction, prediction):
                    self.increment(instruction['address'], table, bits)
                    stats['hit'] += 1
                else:
                    self.decrement(instruction['address'], table, bits)
                    stats['miss'] += 1

        print 'Miss rate: %.3f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')


a = AdaptativePredictor()
a.analyze('gcc_branch.out', 1)