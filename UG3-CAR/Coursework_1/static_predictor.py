
class Predictor(object):

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

    def analyze(self, filename, predictor):
        _file = self.get_file(filename)

        if _file is None:
            print 'Could not analyze %s. File not available.' % filename
            return

        stats = {'hit': 0, 'miss': 0}

        for line in _file:
            instruction = self.tokenize(line)
            prediction = self.predictor(instruction)

            if self.compare(instruction, prediction):
                stats['hit'] += 1
            else:
                stats['miss'] += 1

        _file.close()

        print 'Hit rate: %.3f%s' % (100 * stats['hit'] / float(stats['hit'] + stats['miss']), '%')


class AlwaysTaken(Predictor):

    def predictor(self, instruction):
        """
        Given an Always Taken branch predictor, we always return true.
        """
        return 1


class NeverTaken(Predictor):

    def predictor(self, instruction):
        """
        Always Not Taken predictor always returns false.
        """
        return 0


class ProfileGuided(Predictor):

    def predictor(self, instruction, profile):
        address = str(instruction['address'])
        # print address
        if address in profile.keys():
            if profile[address] >= 0:
                return 1
            else:
                return 0
        print 'Address not found. Something went wrong in profiling'
        return 0

    def profile(self, filename):
        _file = self.get_file(filename)

        if _file is None:
            print 'Could not analyze %s. File not available.' % filename
            return

        stats = {}

        for line in _file:
            instruction = self.tokenize(line)
            address = str(instruction['address'])
            taken = instruction['taken']
            if address in stats.keys():
                stats[address] += 1 if taken == 1 else -1
            else:
                stats[address] = 1 if taken == 1 else -1

        _file.close()
        # print stats
        return stats

    def analyze(self, filename):
        profile = self.profile(filename)
        _file = self.get_file(filename)

        stats = {'hit': 0, 'miss': 0}

        for line in _file:
            instruction = self.tokenize(line)
            prediction = self.predictor(instruction, profile)
            # print 'Prediction:', prediction
            if self.compare(instruction, prediction):
                stats['hit'] += 1
            else:
                stats['miss'] += 1

        _file.close()

        print 'Miss rate: %.3f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')





# a = AlwaysTaken()
# a.analyze('gcc_branch.out', a.predictor)

# b = NeverTaken()
# b.analyze('gcc_branch.out', b.predictor)

p = ProfileGuided()
p.analyze('countTest2.out')