import scala.collection.immutable.Set

import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.syntactical.StandardTokenParsers

object CW1 {
  type Variable = String
  type Env[A] = Map[Variable,A]

  // Arithmetic expressions

  abstract class Expr
  case class Num(n: Integer) extends Expr
  case class Plus(e1: Expr, e2: Expr) extends Expr
  case class Minus(e1: Expr, e2: Expr) extends Expr
  case class Times(e1: Expr, e2: Expr) extends Expr

  // Booleans
  case class Bool(n: Boolean) extends Expr
  case class Eq(e1: Expr, e2:Expr) extends Expr
  case class IfThenElse(e: Expr, e1: Expr, e2: Expr) extends Expr

   // Strings
  case class Str(s: String) extends Expr
  case class Length(e: Expr) extends Expr
  case class Index(e1: Expr, e2: Expr) extends Expr
  case class Concat(e1: Expr, e2: Expr) extends Expr

  // Variables and let-binding
  case class Var(x: Variable) extends Expr
  // let x = e1 in e2
  case class Let(x: Variable, e1: Expr, e2: Expr) extends Expr
  case class LetFun(f: Variable, arg: Variable, ty: Type, e1:Expr, e2:Expr)
      extends Expr
  case class LetRec(f: Variable, arg: Variable, xty: Type, ty: Type, e1:Expr, e2:Expr)
      extends Expr
  case class LetPair(x: Variable,y: Variable, e1:Expr, e2:Expr) extends Expr

  // Pairing
  case class Pair(e1: Expr, e2: Expr) extends Expr
  case class First(e: Expr) extends Expr
  case class Second(e: Expr) extends Expr

  // Functions
  case class Lambda(x: Variable, ty: Type, e: Expr) extends Expr
  case class Apply(e1: Expr, e2: Expr) extends Expr
  case class Rec(f: Variable, x: Variable, tyx:Type, ty: Type, e: Expr) extends Expr

  // Values
  abstract class Value
  case class NumV(n: Integer) extends Value
  case class BoolV(n: Boolean) extends Value
  case class StringV(s: String) extends Value
  case class PairV(v1: Value, v2: Value) extends Value
  case class ClosureV(env: Env[Value], x: Variable, e: Expr) extends Value
  case class RecV(env: Env[Value], f:Variable, x: Variable, e: Expr) extends Value

  // Types
  abstract class Type
  case object IntTy extends Type
  case object BoolTy extends Type
  case object StringTy extends Type
  case class PairTy(ty1: Type, ty2: Type) extends Type
  case class FunTy(ty1: Type, ty2: Type) extends Type

  // ======================================================================
  // Part 1: Syntactic transformation
  // ======================================================================

  // ======================================================================
  // Exercise 1: Capture-avoiding substitution
  // ======================================================================

  // This object provides a method to generate a "fresh" variable name
  object Gensym {
    private var id = 0
    def gensym(s: Variable): Variable = {
      val fresh_s = s + "_" + id
      id = id + 1
      fresh_s
    }
  }


  // In e1, substitute e2 for x
  def subst(e1:Expr, e2:Expr, x: Variable): Expr =
    e1 match {
      case Num(e) => Num(e)
      case Plus(t1,t2) => Plus(subst(t1,e2,x),subst(t2,e2,x))
      case Minus(t1,t2) => Minus(subst(t1,e2,x),subst(t2,e2,x))
      case Times(t1,t2) => Times(subst(t1,e2,x),subst(t2,e2,x))

      case Bool(n) => Bool(n)
      case Eq(l, r) => Eq(subst(l, e2, x), subst(r, e2, x))
      case IfThenElse(cond, yes, no) => IfThenElse(
        subst(cond, e2, x),
        subst(yes, e2, x),
        subst(no, e2, x)
      )

      case Var(y) =>
        if (x == y) {
          e2
        } else {
          Var(y)
        }
      case Let(y, t1, t2) =>
        if (x == y) { // we can stop early since x is re-bound here
          Let(y, subst(t1, e2, x),t2)
        } else { // otherwise, we freshen y
          val z = Gensym.gensym(y);
          val fresh_t2 = subst(t2, Var(z), y);
          Let(z, subst(t1, e2, x), subst(fresh_t2, e2, x))
        }
      case LetFun(f, arg, ty, t1, t2) => sys.error("subst: todo")
      case LetRec(f, arg, xty, ty, t1, t2) => sys.error("subst: todo")
      case LetPair(var1, var2, exp1, exp2) => sys.error("subst: todo")

      case Pair(expr1, expr2) => Pair(subst(expr1, e2, x), subst(expr2, e2, x))
      case First(e) => First(subst(e, e2, x))
      case Second(e) => Second(subst(e, e2, x))

      case Lambda(variable, varType, expr) => {
        if (variable == x) Lambda(variable, varType, subst(expr, e2, variable))
        else {
          var fresh = Gensym.gensym(variable)
          Lambda(fresh, varType, subst(expr, e2, fresh))
        }
      }
      case Apply(expr1, expr2) => Apply(subst(expr1, e2, x), subst(expr2, e2, x))
      case Rec(f, v, tyx, ty, expr) => sys.error("subst: todo")

      case _ => sys.error("Failed to match an Expr case, forgot to implement a case class?")
    }


  // ======================================================================
  // Exercise 2: Desugaring let fun, let rec and let pair
  // ======================================================================

  def desugar(e: Expr): Expr = {
    // TODO: Remove print
    println(e)

    e match {

    case Num(n) => Num(n)
    case Plus(e1,e2) => Plus(desugar(e1),desugar(e2))
    case Minus(e1,e2) => Minus(desugar(e1),desugar(e2))
    case Times(e1,e2) => Times(desugar(e1),desugar(e2))

    case Bool(n) => Bool(n)
    case Length(k) => Length(k)
    case Index(e1, e2) => Index(desugar(e1), desugar(e2))
    case Concat(e1, e2) => Concat(desugar(e1), desugar(e2))

    case Var(v) => Var(v)
    case Let(x, e1, e2) => Let(x, desugar(e1), desugar(e2))
    // let fun f(argVar) = e1 in e2  =>  let f = \argVar. e1 in e2
    case LetFun(funcName, argVar, argType, e1, e2) =>
      Let(funcName, Lambda(argVar, argType, e1), e2)
    // let rec f(argVar) = e1 in e2  =>  let f = rec f(argVar) e1 in e2
    case LetRec(funcName, argVar, argType, recType, e1, e2) =>
      Let(funcName, Rec(funcName, argVar, argType, recType, e1), e2)
    // Let(x: Variable, e1: Expr, e2: Expr)
    // LetPair(x: Variable,y: Variable, e1:Expr, e2:Expr)
    case LetPair(x, y, pairExpression, e2) => {
      val p = Gensym.gensym("p")
      Let(p, desugar(pairExpression), subst(
        subst(e2, First(Var(p)), x),
        Second(Var(p)), y
      ))
    }

    case Pair(e1, e2) => Pair(desugar(e1), desugar(e2))
    case First(e1) => First(desugar(e1))
    case Second(e1) => Second(desugar(e1))

    case Lambda(x, ty, e1) => Lambda(x, ty, desugar(e1))
    case Apply(e1, e2) => Apply(desugar(e1), desugar(e2))
    case Rec(f, x, tyx, ty, e1) => Rec(f, x, tyx, ty, desugar(e1))

    case _ => sys.error("Failed to match Expr type to desugar.")
  }}


  // ======================================================================
  // Part 2: Interpretation
  // ======================================================================

  // ======================================================================
  // Exercise 3: Primitive operations
  // ======================================================================


  object Value {
    // utility methods for operating on values
    def add(v1: Value, v2: Value): Value = (v1,v2) match {
      case (NumV(v1), NumV(v2)) => NumV (v1 + v2)
      case _ => sys.error("arguments to addition are non-numeric")
    }

    def subtract(v1: Value, v2: Value): Value = (v1,v2) match {
      case (NumV(v1), NumV(v2)) => NumV (v1 - v2)
      case _ => sys.error("arguments to addition are non-numeric")
    }

    def multiply(v1: Value, v2: Value): Value = (v1, v2) match {
      case (NumV(v1), NumV(v2)) => NumV(v1 * v2)
      case _ => sys.error("arguments to multiplication are non-numeric")
    }

    def eq(v1: Value, v2: Value): Value = (v1, v2) match {
      case (NumV(n1), NumV(n2)) => BoolV(n1 == n2)
      case (BoolV(b1), BoolV(b2)) => BoolV(b1 == b2)
      case (StringV(s1), StringV(s2)) => BoolV(s1 == s2)
      case _ => sys.error("arguments to equality are not one of [NumV, BoolV, StringV]")
    }

    def length(v: Value): Value = v match {
      case StringV(v) => NumV(v.length())
      case _ => sys.error("length can be only be called on StringV types")
    }

    def index(v1: Value, v2: Value): Value = (v1, v2) match {
      case (StringV(s1), NumV(n1)) => StringV(s1.charAt(n1).toString())
      case _ => sys.error("cannot get index of given types, [StringV, NumV] are required")
    }

    def concat(v1: Value, v2: Value): Value = (v1, v2) match {
      case (StringV(s1), StringV(s2)) => StringV(s1 + s2)
      case _ => sys.error("only StringV types can be concatenated")
    }
  }




  // ======================================================================
  // Exercise 4: Evaluation
  // ======================================================================

  def eval (env: Env[Value], e: Expr): Value = e match {
    // Arithmetic
    case Num(n) => NumV(n)
    case Plus(e1, e2) => Value.add(eval(env, e1), eval(env, e2))
    case Minus(e1, e2) => Value.subtract(eval(env, e1), eval(env, e2))
    case Times(e1, e2) => Value.multiply(eval(env, e1), eval(env, e2))

    case Bool(b) => BoolV(b)
    case Eq(e1, e2) => Value.eq(eval(env, e1), eval(env, e2))
    case IfThenElse(e, e1, e2) => eval(env, e) match {
      case BoolV(true) => eval(env, e1)
      case BoolV(false) => eval(env, e2)
    }

    case Str(s) => StringV(s)
    case Length(s) => Value.length(eval(env, s))
    case Index(e1, e2) => Value.index(eval(env, e1), eval(env, e2))
    case Concat(e1, e2) => Value.concat(eval(env, e1), eval(env, e2))

    case Pair(e1, e2) => PairV(eval(env, e1), eval(env, e2))
    case First(Pair(e1, e2)) => eval(env, e1)
    case Second(Pair(e1, e2)) => eval(env, e2)

    // TODO: Functions
    case Lambda(x, ty, e) => sys.error("eval: todo")
    case Apply(e1, e2) => sys.error("eval: todo")
    case Rec(f, x, tyx, ty, e) => sys.error("eval: todo")

    case _ => sys.error("Failed to match expression type.")
  }


  // ======================================================================
  // Part 3: Typechecking
  // ======================================================================

  // ======================================================================
  // Exercise 5: Typechecker
  // ======================================================================

  // typing: calculate the return type of e, or throw an error
  def tyOf(ctx: Env[Type], e: Expr): Type = e match {
    // Arithmetic
    case Num(n) => IntTy
    case Plus(e1,e2) => (tyOf(ctx,e1),tyOf(ctx,e2)) match {
      case (IntTy, IntTy) => IntTy
      case _ => sys.error("non-integer arguments to -")
    }

    // Variables and let-binding
    case Var(x) => ctx(x)
    case Let(x,e1,e2) => tyOf(ctx + (x -> (tyOf(ctx,e1))), e2)

    case _ => sys.error("tyOf: todo")
  }


  // ======================================================================
  // Part 4: Some simple programs
  // ======================================================================

  // The following examples illustrate how to embed L_CW1 source code into
  // Scala using multi-line comments, and parse it using parser.parseStr.

  // Example 1: the swap function
  def example1: Expr = parser.parseStr("""
    let fun swap(x:(int,int)) = (snd(x), fst(x)) in
    swap(42,17)
    """)

  // Example 2: the factorial function, yet again
  def example2: Expr = parser.parseStr("""
    let rec fact(n:int):int =
      if (n == 0) then 1 else n * fact(n - 1) in
    fact(5)
    """)

  // Example 3: exponentiation
  def example3: Expr = parser.parseStr("""
    let rec power(input:(int,int)):int =
          let (x,n) = input in
          if (n == 0) then 1 else
          x * power(x,n-1)
        in
        power(2,10)
    """)

  // Example 4: check whether two strings have the same last character
  def example4: Expr = parser.parseStr("""
    let fun sameLastChar(input:(str,str)) =
      let (s1,s2) = input in
      index(s1,length(s1)-1) == index(s2,length(s2)-1)
    in sameLastChar("abcz","abcdefghijklmnopqrstuvwxyz")
    """)


  // ======================================================================
  // Exercise 6: Fibonacci sequence
  // ======================================================================

  def fib: Expr = sys.error("fib: todo")


  // ======================================================================
  // Exercise 7: Substrings
  // ======================================================================

  def substring: Expr = sys.error("substring: todo")


  /*======================================================================
   The rest of this file is support code, which you should not (and do not
   need to) understand or change.
   ====================================================================== */

  class CWParser extends StandardTokenParsers with PackratParsers {

    type P[+A] = PackratParser[A]

    def parseStr(input: String): Expr = {
      phrase(expression)(new lexical.Scanner(input)) match {
        case Success(ast, _) => desugar(ast)
        case e: NoSuccess => sys.error(e.msg)
      }
    }

    def parse(input: String): Expr = {
      val source = scala.io.Source.fromFile(input)
      val lines = try source.mkString finally source.close()
      parseStr(lines)
    }

    lexical.reserved += ("let", "in", "rec", "if", "then", "else",
      "int","str","bool","true","false","fst","snd","concat",
      "index","length","fun"
    )
    lexical.delimiters += ("=","*", "\\", "+", "-", "(", ")", "==", ":", ".",
      "->", ","
    )

    lazy val expression: P[Expr] =
      simpleExpr

    lazy val lambda: P[Expr] =
      ("\\" ~> ident) ~ (":" ~> typ) ~ ("." ~> expression) ^^ {
        case arg~ty~body => Lambda(arg,ty,body)
      }

    lazy val rec: P[Expr] =
      ("rec" ~> ident) ~
        ("(" ~> ident) ~ (":" ~> typ) ~ ((")" ~ ":") ~> typ) ~
        ("." ~> expression) ^^ {
          case recArg~funArg~funType~recType~body =>
            Rec(recArg,funArg,funType,recType,body)
        }

    lazy val ifExpr: P[Expr] =
      ("if" ~> expression) ~
        ("then" ~> expression) ~
        ("else" ~> expression) ^^ {
          case cond~e1~e2 => IfThenElse(cond,e1,e2)
        }

    lazy val letExpr: P[Expr] =
      ("let" ~> ident) ~ ("=" ~> expression) ~ ("in" ~> expression) ^^ {
        case binder~e1~e2 => Let(binder,e1,e2)
      }

    lazy val letFun: P[Expr] =
      ("let" ~ "fun" ~> ident) ~ ("(" ~> ident) ~
        (":" ~> typ <~ ")") ~ ("=" ~> expression) ~
        ("in" ~> expression) ^^ {
          case fun~binder~ty~e1~e2 => LetFun(fun,binder,ty,e1,e2)
        }

    lazy val letRec: P[Expr] =
      ("let" ~ "rec" ~> ident) ~ ("(" ~> ident) ~
        (":" ~> typ <~ ")") ~ (":" ~> typ) ~ ("=" ~> expression) ~
        ("in" ~> expression) ^^ {
          case fun~binder~xty~ty~e1~e2 => LetRec(fun,binder,xty,ty,e1,e2)
        }

    lazy val letPair: P[Expr] =
      ("let" ~ "(") ~> ident ~ ("," ~> ident <~ ")") ~
        ("=" ~> expression) ~ ("in" ~> expression) ^^ {
          case x~y~e1~e2 => LetPair(x,y,e1,e2)
        }

    lazy val typ: P[Type] =
      funTyp

    lazy val funTyp: P[Type] =
      pairTyp ~ "->" ~ funTyp ^^ {
        case t1~"->"~t2 => FunTy(t1,t2)
      } | pairTyp

    lazy val pairTyp: P[Type] =
      primitiveType ~ "*" ~ pairTyp ^^ {
        case t1~"*"~t2 => PairTy(t1,t2)
      } | primitiveType

    lazy val primitiveType: P[Type] =
      "bool" ^^^ BoolTy | "int" ^^^ IntTy | "str" ^^^ StringTy |  "("~>typ<~")"


    lazy val operations: P[Expr] =
      application |
      ("fst" ~ "(") ~> expression <~ ")" ^^ (x => First(x)) |
        ("snd" ~ "(") ~> expression <~ ")" ^^ (x => Second(x)) |
        ("length" ~ "(") ~> expression <~ ")" ^^ (x => Length(x)) |
        ("concat"  ~ "(") ~> expression ~ ("," ~> expression) <~ ")" ^^ {
          case e1~e2 => Concat(e1,e2)
        } |
        ("index" ~ "(") ~> expression ~ ("," ~> expression) <~ ")" ^^ {
          case e1~e2 => Index(e1,e2)
        }

    lazy val arith: P[Expr] =
      eq

    lazy val prod: P[Expr] =
      prod ~ "*" ~ fact ^^ {
        case e1~"*"~e2 => Times(e1,e2)
      } | fact

    lazy val summation: P[Expr] =
      summation ~ "+" ~ prod ^^ {
        case e1~"+"~e2 => Plus(e1,e2)
      } | summation ~ "-" ~ prod ^^ {
        case e1~"-"~e2 => Minus(e1,e2)
      } | prod

    lazy val eq: P[Expr] =
      simpleExpr ~ "==" ~ summation ^^ {
        case e1~"=="~e2 => Eq(e1,e2)
      } | summation

    lazy val application: P[Expr] =
      fact ~ fact ^^ {
        case e1~e2 => Apply(e1,e2)
      }

    lazy val simpleExpr: P[Expr] = (
      lambda |
        rec |
        letExpr |
        letFun |
        letRec |
        letPair |
        ifExpr |
        arith |
        fact
    )

    lazy val pairLit: P[Expr] =
      "(" ~> expression ~ "," ~ expression <~ ")" ^^ {
        case t1~","~t2 => Pair(t1,t2)
      }

    lazy val fact: P[Expr] = (
      operations |
        pairLit |
        (ident ^^ Var) |
        (numericLit ^^ { x => Num(x.toInt) }) |
        (stringLit ^^ Str) |
        ("true" ^^^ Bool(true)) |
        ("false" ^^^ Bool(false)) |
        "("~>expression<~")"
    )

  }


  val parser = new CWParser


  object Main {
    def typecheck(ast: Expr):Type =
      tyOf(Map.empty,ast);

    def evaluate(ast: Expr):Value =
      eval(Map.empty,ast)



    def showResult(ast: Expr) {
      println("AST:  " + ast.toString + "\n")

      print("Type Checking...");
      val ty = typecheck(ast);
      println("Done!");
      println("Type of Expression: " + ty.toString + "\n") ;

      println("Result: " + evaluate(ast));
    }

    def start(): Unit = {
      println("Welcome to Giraffe!");
      println("Enter expressions to evaluate, :load <filename.gir> to load a file, or :quit to quit.");
      repl()
    }

    def repl(): Unit = {
      print("CW1> ");
      val input = scala.io.StdIn.readLine();
      if(input == ":quit") {
        println("Goodbye!")
      }
      else if (input.startsWith(":load")) {
        val ast = parser.parse(input.substring(6));
        showResult(ast);
        repl()
      } else {
        try {
          val ast = parser.parseStr(input);
          showResult(ast)
        } catch {
          case e:Throwable => println("Error: " + e)
        }
        repl()
      }
    }

  }
  def main( args:Array[String] ):Unit = {
    if(args.length == 0) {
      Main.start()
    } else {
      try {
        print("Parsing...");
        val ast = parser.parse(args.head)
        println("Done!");
        Main.showResult(ast)
      } catch {
        case e:Throwable => println("Error: " + e)
      }
    }
  }
}



