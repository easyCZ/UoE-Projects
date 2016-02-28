from models import State, Action


class Protocol(object):
    pass

class MSI(Protocol):
    """
    Implements the MSI cache coherency protocol.
    """

    def map(self, local, remotes, action):
        return (self.local(local, action), map(lambda r: self.remote(r, action), remotes))

    def remote(self, state, action):
        """
        Given current state, return the next state for a remote CPU
        """
        # From Shared
        if state is State.shared and action is Action.read_miss:
            return State.shared
        elif state is State.shared and action is Action.write_miss:
            return State.invalid

        # From Modified
        elif all([state is State.modified, action is Action.read_miss]):
            return State.shared

        elif all([state is State.modified, action is Action.write_miss]):
            return State.invalid

        return state

    def local(self, state, action):
        """
        Given current state, return the next state for the local CPU
        """
        # From Invalid state
        if all([state is State.invalid, action is Action.read_miss]):
            return State.shared
        elif all([state is State.invalid, action is Action.write_miss]):
            return State.modified

        # From Shared state
        elif all([state is state.shared, action is Action.read_hit]):
            return State.shared
        elif all([state is state.shared, action is Action.write_miss]):
            return State.modified
        elif all([state is state.shared, action is Action.write_hit]):
            return State.modified

        # From Modified state
        elif all([state is state.modified, action is Action.read_hit]):
            return State.modified
        elif all([state is state.modified, action is Action.write_hit]):
            return State.modified

        return state

    def __repr__(self):
        return 'MSI'


class MESI(Protocol):
    """
    Implements the MESI cache coherence protocol
    """

    def local(self, state, action, shared):
        """
        Transition to the new state in the local CPU.

        Params:
            state   current state
            action  action
            shared  flag to indicate if other cache has a copy
        """
        pass

    def remote(self, state, action):
        is_modified = state is State.modified
        return state

    def __repr__(self):
        return 'MESI'


class MES(Protocol):
    pass
