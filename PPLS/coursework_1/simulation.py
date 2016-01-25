import random


class One(object):

    def __init__(self):
        self.exec_count = 0

    def can_execute(self, x, y):
        return True

    def execute(self, x, y):
        print('Executing ONE', x, y)
        while x != y:
            x = x - 1
            self.exec_count += 1
            if self.exec_count > 100:
                return (x, y)
        y = y + 1

        print('Executed ONE', x, y)
        self.exec_count += 1
        return (x, y)


class Two(object):

    def __init__(self):
        self.exec_count = 0

    def can_execute(self, x, y):
        return x == y

    def execute(self, x, y):
        print('Executing TWO', x, y)
        x = 8
        y = 2

        self.exec_count += 1
        print('Executed TWO', x, y)
        return (x, y)



def run():
    x = 10
    y = 0

    one = One()
    two = Two()
    is_blocked = False

    while not is_blocked:
        rand = random.random()
        selected = None

        if rand < 0.5:
            selected = one
        else:
            selected = two

        if selected.can_execute(x, y):
            x, y = selected.execute(x, y)

        if selected.exec_count > 100:
            is_blocked = True

    print('Final x,y', x, y)

run()
