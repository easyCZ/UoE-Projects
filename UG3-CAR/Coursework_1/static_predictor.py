#   Computer Architecture 2014 - University of Edinburgh
#   Student ID: s1115104
import argparse


class Predictor(object):

    def tokenize(self, line):
        """
        Split a line of input into tokens.
        """
        tokens = line.split()
        return {'address': tokens[1], 'taken': int(tokens[2])}

    def get_file(self, filename):
        """
        Attempt to open a file. Prints an error if exception is raised.
        """
        try:
            return open(filename)
        except Exception, e:
            print 'Could not open %s' % filename

    def compare(self, instruction, prediction):
        """
        Compare the actual instruction flag with the prediction.
        """
        return instruction['taken'] == prediction

    def analyze(self, filename, predictor):
        """
        Analyze the filename given a predictor.

        Params:
            [string] filename       Name of the file to analyze
            [function] predictor    Function to be used for prediction

        Returns:
            Statistics of the predictor analysis are printed.
        """
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

        print 'Miss rate: %.3f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')


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
    """
    Class used for analysis of a branch predictor based on profiling.

    First profile a file and record the counts for each branch taken and not taken.
    If there are more branches taken for an address then the branch will be predicted
    to be taken. Otherwise, not taken.
    """

    def predictor(self, instruction, profile):
        """
        Predict whether a branch will be taken.

        Workings:
            profile contains addresses as keys with a value of 'taken - not taken'.
            If positive, there are more takens and we predict to take the branch.
            Otherwise, we predict to not take the branch.

        Returns:
            0 or 1 based on the prediction and profile.
        """
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
        """
        Profile a file. Record all addresses as keys with relative counts associated to each key.
        Taken translates to +1 while not taken translates to -1 added to the count for each address.

        Params:
            [string] filename   the file to analyze

        Returns:
            [dict] stats    dictionary with relative counts
        """
        _file = self.get_file(filename)

        if _file is None:
            print 'Could not analyze %s. File not available.' % filename
            return

        stats = {}

        for line in _file:
            instruction = self.tokenize(line)
            address = instruction['address']
            taken = instruction['taken']

            if address in stats.keys():
                stats[address] += 1 if taken == 1 else -1
            else:
                stats[address] = 1 if taken == 1 else -1

        _file.close()
        # print stats
        return stats

    def analyze(self, filename):
        """
        Main analysis of the file.
        """
        # Build a profile
        profile = self.profile(filename)

        # Open a file
        _file = self.get_file(filename)
        stats = {'hit': 0, 'miss': 0}

        for line in _file:
            instruction = self.tokenize(line)   # break line into a dict
            prediction = self.predictor(instruction, profile)   # make a prediction

            if self.compare(instruction, prediction):   # compare the prediction with reality
                stats['hit'] += 1
            else:
                stats['miss'] += 1

        _file.close()

        print 'Miss rate: %.3f%s' % (100 * stats['miss'] / float(stats['hit'] + stats['miss']), '%')

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('filename', help='File to analyze by the predictor.')
    parser.add_argument('method', help='The type of predictor method to be used. Valid options are ["alwaystaken", "nevertaken", "profile"]')

    args = parser.parse_args()

    if args.method.lower() in ['alwaystaken', 'nevertaken', 'profile']:
        if args.method.lower() == 'alwaystaken':
            a = AlwaysTaken()
            a.analyze('gcc_branch.out', a.predictor)
        elif args.method.lower() == 'nevertaken':
            b = NeverTaken()
            b.analyze('gcc_branch.out', b.predictor)
        else:
            p = ProfileGuided()
            p.analyze('gcc_branch.out')
    else:
        print 'Incorrect predictor method. Please use one of ["alwaystaken", "nevertaken", "profile"]'
