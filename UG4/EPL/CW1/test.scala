
// Values
val one = CW1.NumV(1)
val two = CW1.NumV(2)
val three = CW1.NumV(3)

val yes = CW1.BoolV(true)
val no = CW1.BoolV(false)
val word = CW1.StringV("word")
val sentence = CW1.StringV("this is a sentence")

// Multiply
CW1.Value.multiply(two, three) == CW1.NumV(6)


// Equal
CW1.Value.eq(one, two) == CW1.BoolV(false)
CW1.Value.eq(one, one) == CW1.BoolV(true)
CW1.Value.eq(yes, yes) == CW1.BoolV(true)
CW1.Value.eq(yes, no) == CW1.BoolV(false)
CW1.Value.eq(no, no) == CW1.BoolV(true)
CW1.Value.eq(word, word) == CW1.BoolV(true)
CW1.Value.eq(word, sentence) == CW1.BoolV(false)

// Length
CW1.Value.length(word) == CW1.NumV(4)
CW1.Value.length(sentence) == CW1.NumV(18)

// Index
CW1.Value.index(sentence, two) == CW1.StringV("i")
CW1.Value.index(sentence, one) == CW1.StringV("h")
CW1.Value.index(sentence, CW1.NumV(17)) == CW1.StringV("e")

// Concat
CW1.Value.concat(sentence, word) == CW1.StringV("this is a sentenceword")