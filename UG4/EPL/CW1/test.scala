import collection.immutable.ListMap

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


// Exercise 4 - EVALUATION
def getEnv(): ListMap[CW1.Variable, CW1.Value] = ListMap[CW1.Variable, CW1.Value]()
val env = getEnv()

val oneNum = CW1.Num(1)
val twoNum = CW1.Num(2)
val threeNum = CW1.Num(3)
val onePlusTwo = CW1.Plus(oneNum, twoNum)
val twoTimesThree = CW1.Times(twoNum, threeNum)

val yesExpr = CW1.Bool(true)
val noExpr = CW1.Bool(false)
val yesEqualsNoExpr = CW1.Eq(yesExpr, noExpr)
val ifYesEqualsNoThenTwoTimesThreeElseOnePlusTwo = CW1.IfThenElse(
    yesEqualsNoExpr, twoTimesThree, onePlusTwo
)

CW1.eval(env, onePlusTwo) == three
CW1.eval(env, twoTimesThree) == CW1.NumV(6)
CW1.eval(env, yesEqualsNoExpr) == no
CW1.eval(env, ifYesEqualsNoThenTwoTimesThreeElseOnePlusTwo) == three