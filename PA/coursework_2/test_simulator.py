import unittest
from simulator import MSI
from models import State, Action

class MSITest(unittest.TestCase):

    def setUp(self):
        self.sut = MSI()

    def test_this_cpu_from_invalid_on_read_miss(self):
        new_state = self.sut.this_cpu(State.invalid, Action.read_miss)
        self.assertEqual(State.shared, new_state)

    def test_this_cpu_from_shared_on_read_hit(self):
        new_state = self.sut.this_cpu(State.shared, Action.read_hit)
        self.assertEqual(State.shared, new_state)

    def test_this_cpu_from_shared_on_write_hit(self):
        new_state = self.sut.this_cpu(State.shared, Action.write_hit)
        self.assertEqual(State.modified, new_state)

    def test_this_cpu_from_shared_on_write_miss(self):
        new_state = self.sut.this_cpu(State.shared, Action.write_miss)
        self.assertEqual(State.modified, new_state)

    def test_modified_on_read_hit(self):
        new_state = self.sut.this_cpu(State.modified, Action.read_hit)
        self.assertEqual(State.modified, new_state)

    def test_modified_on_write_hit(self):
        new_state = self.sut.this_cpu(State.modified, Action.write_hit)
        self.assertEqual(State.modified, new_state)

    def test_invalid_on_write_miss(self):
        new_state = self.sut.this_cpu(State.invalid, Action.write_miss)
        self.assertEqual(State.modified, new_state)
