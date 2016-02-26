import unittest
from protocols import MSI
from models import State, Action

class ProtocolTest(unittest.TestCase):

    def assert_local(self, current_state, action, new_state):
        state = self.sut.local(current_state, action)
        return self.assertEqual(new_state, state)

    def assert_remote(self, current_state, action, new_state):
        state = self.sut.remote(current_state, action)
        return self.assertEqual(new_state, state)

class MSITest(ProtocolTest):

    def setUp(self):
        self.sut = MSI()

    def test_local_from_invalid_on_read_miss(self):
        self.assert_local(State.invalid, Action.read_miss, State.shared)

    def test_local_from_shared_on_read_hit(self):
        self.assert_local(State.shared, Action.read_hit, State.shared)

    def test_local_from_shared_on_write_hit(self):
        self.assert_local(State.shared, Action.write_hit, State.modified)

    def test_local_from_shared_on_write_miss(self):
        self.assert_local(State.shared, Action.write_miss, State.modified)

    def test_local_modified_on_read_hit(self):
        self.assert_local(State.modified, Action.read_hit, State.modified)

    def test_local_modified_on_write_hit(self):
        self.assert_local(State.modified, Action.write_hit, State.modified)
    def test_local_invalid_on_write_miss(self):
        self.assert_local(State.invalid, Action.write_miss, State.modified)

    # Other CPUs
    def test_remote_shared_on_write_miss(self):
        self.assert_remote(State.shared, Action.write_miss, State.invalid)

    def test_remote_shared_on_read_miss(self):
        self.assert_remote(State.shared, Action.read_miss, State.shared)

    def test_remote_modified_on_read_miss(self):
        self.assert_remote(State.modified, Action.read_miss, State.shared)

    def test_remote_modified_on_write_miss(self):
        self.assert_remote(State.modified, Action.write_miss, State.invalid)