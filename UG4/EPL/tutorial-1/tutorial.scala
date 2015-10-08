abstract class Expr
case class Num(n: Integer) extends Expr
case class Plus(e1: Expr, e2: Expr) extends Expr
case class Times(e1: Expr, e2: Expr) extends Expr

// 1. (a)
def evens[A](l: List[A]): List[A] = l match {
    case x :: y :: ys => y :: events(ys)
    case x :: ys => ys
    case Nil => List[A]()
}

def testEvens(): Boolean = {
    evens(List('a','b','c','d','e','f')) == List('b', 'd', 'f') &&
    evens(List()) == List() &&
    evens(List('a')) == List() &&
    evens(List('a', 'b')) == List('b') &&
    evens(List('a', 'b', 'c')) == List('b')
}

// 1. (b)
def allplus(e: Expr): Boolean = e match {
    case Num(n) => true
    case Plus(e1, e2) => allplus(e1) && allplus(e2)
    case _ => false
}

def testAllPlus(): Boolean = {
    allplus(Plus(Num(1), Num(2))) == true &&
    allplus(Times(Num(1), Num(2))) == false &&
    allplus(Num(1)) == true
}


// 1. (c)
def consts(e: Expr): List[Int] = e match {
    case Num(n) => List(n)
    case Plus(e1, e2) => consts(e1) ++ consts(e2)
    case Times(e1, e2) => consts(e1) ++ consts(e2)
}

def testConsts(): Boolean = {
    consts(Num(1)) == List(1) &&
    consts(Plus(Num(1), Num(2))) == List(1, 2) &&
    consts(Times(Num(1), Num(2))) == List(1, 2) &&
    consts(Times(Plus(Num(1), Num(2)), Num(3))) == List(1, 2, 3)
}

// 1. (d)
def revtimes(e: Expr): Expr = e match {
    case Num(n) => Num(n)
    case Plus(e1, e2) => Plus(revtimes(e1), revtimes(e2))
    case Times(e1, e2) => Times(revtimes(e2), revtimes(e1))
}

def testRevtimes(): Boolean = {
    revtimes(Num(1)) == Num(1) &&
    revtimes(Plus(Num(1), Num(2))) == Plus(Num(1), Num(2)) &&
    revtimes(Times(Num(1), Num(2))) == Times(Num(2), Num(1)) &&
    revtimes(Plus(Times(Num(1), Num(2)), Num(3))) == Plus(Times(Num(2), Num(1)), Num(3))
}

// 1. (e)
def printExpr(e: Expr): String = e match {
    case Num(n) => n.toString()
    case Plus(e1, e2) => "(" + printExpr(e1) + " + " + printExpr(e2) + ")"
    case Times(e1, e2) => "(" + printExpr(e1) + " * " + printExpr(e2) + ")"
}

def testPrintExpr(): Boolean = {
    printExpr(
        Plus(
            Times(Num(1), Num(2)),
            Num(3)
        )
    ) == "(1 + 2) * 3"
}