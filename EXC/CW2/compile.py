#!/usr/bin/python

import argparse


class ReportGenerator(object):

    INCLUDE_PREFIX = '{ include'

    def __init__(self, template, output=None):
        self.template = template
        self.output_filename = output
        self.output_file = None

    def write(self, what):
        if self.output_file:
            self.output_file.write(what)
        else:
            print(what.replace('\n', ''))

    def write_include(self, filename):
        with open(filename) as f:
            for line in f:
                self.write(line)

    def compile(self):
        if self.output_filename:
            self.output_file = open(self.output_filename, 'w')

        with open(self.template) as template:
            for line in template:
                if self._is_include(line):
                    filename = self._get_include(line)
                    self.write_include(filename)
                else:
                    self.write(line)

        if self.output_file:
            print('File saved to %s' % self.output_filename)
            self.output_file.close()

    def _is_include(self, line):
        return line.strip().startswith(self.INCLUDE_PREFIX)

    def _get_include(self, line):
        """
        Retrieve content of include.

        Input: "{ include task1/test.py }"
        Output: "task1/test.py"
        """
        return line.replace('{', '').replace('}', '').replace('include', '').strip()



if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Compile report from individual templates.')
    parser.add_argument('-template', type=str, help='Top level template to compile.', required=True)
    parser.add_argument('-out', type=str, help='Output file to write to')

    args = parser.parse_args()
    generator = ReportGenerator(args.template, args.out)
    generator.compile()