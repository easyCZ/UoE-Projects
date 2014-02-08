
class StaticPredictor(object):

    def tokenize(self, line):
        tokens = line.split()
        return {'address': int(tokens[1], 16), 'taken': int(tokens[2])}

    def get_file(self, filename):
        try:
            return open(filename)
        except Exception, e:
            print 'Could not open %s' % filename

    def compare(self, instruction, prediction):
        return instruction['taken'] == prediction


class AlwaysTaken(StaticPredictor):

    def predict(self, instruction):
        """
        Given an Always Taken branch predictor, we always return true.
        """
        return 1



    def analyze(self, filename):
        _file = self.get_file(filename)

        if _file is None:
            print 'Could not analyze %s. File not available.' % filename
            return

        stats = {'hit': 0, 'miss': 0}

        for line in _file:
            instruction = self.tokenize(line)
            prediction = self.predict(instruction)

            if self.compare(instruction, prediction):
                stats['hit'] += 1
            else:
                stats['miss'] += 1

        _file.close()

        print stats
        print stats['hit'] / float(stats['hit'] + stats['miss'])


a = AlwaysTaken().analyze('gcc_branch.out')