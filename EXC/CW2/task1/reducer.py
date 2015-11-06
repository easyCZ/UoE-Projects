#!/usr/bin/python
import sys

word_counter = 0
last_word = None
word_documents = []


def write(keyword, total, documents):
    docs = ", ".join(["(%s, %d)" % (doc, count) for doc, count in documents])
    docs = "{%s}" % docs
    print("{0} : {1} : {2}".format(keyword, total, docs))

# Input is sorted primarily by word, secondarily by
# document name. Single pass is required without doing any
# sorting.
#
# Expected input:
#   also        d1.txt  13
#   also        d3.txt  8
#   also        d4.txt  3
#   altered     d5.txt  1
#   althorp     d5.txt  2
for line in sys.stdin:
    word, doc, count = line.split(' ', 2)

    if last_word is not None and last_word != word:
        # Cleanup after last block
        write(last_word, word_counter, word_documents)
        # Start new block
        word_documents = []
        word_counter = 0

    count = int(count)

    last_word = word
    word_counter += count
    word_documents.append((doc, count))

if last_word is not None:
    write(last_word, word_counter, word_documents)


