import random
import string
import hashlib

RANDOM_STR_SIZE = 4
LAST_BYTES = b'\x00\x91\xeb'
STRING_CHOICE = sum([
	string.ascii_uppercase,
    string.ascii_lowercase,
    string.digits
])

def find_hash():
    value, hash = None, None
    while not value:
        random_str = ''.join(random.choice(STRING_CHOICE) for _ in range(RANDOM_STR_SIZE))
        md5 = hashlib.md5(bytearray(map(ord, random_str))).digest()

        if md5.endswith(LAST_BYTES):
            value = random_str
            hash = md5

    return (value, hash)