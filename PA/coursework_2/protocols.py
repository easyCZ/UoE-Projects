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
        is_write = action is Action.write_miss or action is Action.write_hit
        if state is State.shared and action is Action.read_miss:
            return State.shared
        elif state is State.shared and is_write:
            return State.invalid
        # elif all([state is State.shared, action is Action.write_hit]):
        #     return State.invalid

        # From Modified
        elif all([state is State.modified, action is Action.read_miss]):
            return State.shared


        elif all([state is State.modified, action is Action.write_miss]):
            return State.invalid

        return state

    def local(self, state, action, **kwargs):
        """
        Given current state, return the next state for the local CPU
        """
        # From Invalid state
        if all([state is State.invalid, action is Action.read_miss]):
            return State.shared
        elif all([state is State.invalid, action is Action.write_miss]):
            return State.modified

        # From Shared state
        elif all([state is State.shared, action is Action.read_hit]):
            return State.shared
        elif all([state is State.shared, action is Action.write_miss]):
            return State.modified
        elif all([state is State.shared, action is Action.write_hit]):
            return State.modified

        # From Modified state
        elif all([state is State.modified, action is Action.read_hit]):
            return State.modified
        elif all([state is State.modified, action is Action.write_hit]):
            return State.modified

        return state

    def should_invalidate_others(self, state, action, **kwargs):
        is_write = action is Action.write_hit or action is Action.write_miss
        return state is State.shared and is_write

    def __repr__(self):
        return 'MSI'


class MESI(Protocol):
    """
    Implements the MESI cache coherence protocol
    """

    def local(self, state, action, **kwargs):
        shared = kwargs['shared']
        is_invalid = state is State.invalid
        is_read_miss = action is Action.read_miss
        is_write_miss = action is Action.write_miss
        is_write_hit = action is Action.write_hit
        is_write = is_write_miss or is_write_hit

        if all([is_invalid, is_read_miss, shared]):
            return State.shared
        elif all([is_invalid, is_write_miss]):
            return State.modified
        elif all([is_invalid, is_read_miss, not shared]):
            return State.exclusive

        is_shared = state is State.shared
        is_read_hit = action is Action.read_hit
        if all([is_shared, is_read_hit]):
            return State.shared
        elif all([is_shared, is_write]):
            return State.modified

        is_modified = state is State.modified
        if all([is_modified, is_read_hit or is_write_hit]):
            return State.modified

        is_exclusive = state is State.exclusive
        if all([is_exclusive, is_read_hit]):
            return State.exclusive
        elif all([is_exclusive, is_write]):
            return State.modified

        return state

    def remote(self, state, action):
        is_modified = state is State.modified

        if all([is_modified, action is Action.write_miss]):
            return State.invalid
        elif all([is_modified, action is Action.read_miss]):
            return State.shared

        is_exclusive = state is State.exclusive
        if all([is_exclusive, action is Action.write_miss]):
            return State.invalid
        elif all([is_exclusive, action is Action.read_miss]):
            return State.shared

        is_shared = state is State.shared
        if all([is_shared, action is Action.read_miss]):
            return State.shared
        elif all([is_shared, action is Action.write_miss]):
            return State.invalid

        return state

    def should_invalidate_others(self, state, action, **kwargs):
        is_write = action is Action.write_hit or action is Action.write_miss
        return state is State.shared and is_write

    def __repr__(self):
        return 'MESI'


class MES(Protocol):

    def remote(self, state, action, write_update):

        is_exclusive = state is State.exclusive

        if all([is_exclusive, action is Action.read_miss or action is Action.write_miss]):
            return State.shared

        is_shared = state is State.shared
        if all([is_shared, action is Action.read_miss or action is Action.write_miss or write_update]):
            return State.shared
        if all([state is State.modified, action is Action.read_miss or action is Action.write_miss]):
            return State.shared

        return state

    def local(self, state, action, **kwargs):
        shared = kwargs['shared']
        send_write_update = False

        is_exclusive = state is State.exclusive
        if all([is_exclusive, action is Action.read_hit]):
            return (State.exclusive, send_write_update)
        if all([not shared, action is Action.read_miss]):
            return (State.exclusive, send_write_update)
        if all([is_exclusive, action is Action.write_hit]):
            return (State.modified, send_write_update)

        is_modified = state is State.modified
        if all([is_modified, action is Action.write_hit or action is Action.read_hit]):
            return (State.modified, send_write_update)
        if all([not shared, action is Action.write_miss]):
            return (State.modified, send_write_update)

        is_shared = state is State.shared
        if all([shared, action is Action.read_miss]):
            return (State.shared, send_write_update)
        if all([action is Action.write_miss, shared]):
            send_write_update = True
            return (State.shared, send_write_update)
        if all([is_shared, action is Action.read_hit]):
            return (State.shared, send_write_update)
        if all([is_shared, action is Action.write_miss or action is Action.write_hit]):
            send_write_update = True
            return (State.shared, send_write_update)

        return (state, send_write_update)

    def __repr__(self):
        return 'MES'

