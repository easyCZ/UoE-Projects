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
  // substtute e2 for x in e1
  def subst(e1:Expr, e2:Expr, x: Variable): Expr = e1 match {
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

    case Str(s) => Str(s)
    case Length(e) => Length(subst(e, e2, x))
    case Index(s1, s2) => Index(subst(s1, e2, x), subst(s2, e2, x))
    case Concat(s1, s2) => Concat(subst(s1, e2, x), subst(s2, e2, x))

    case Var(y) =>
      if (x == y) {
        e2
      } else {
        Var(y)
      }
    case Let(y,t1,t2) => {
      if (x == y) { // we can stop early since x is re-bound here
        Let(y,subst(t1,e2,x),t2)
      } else { // otherwise, we freshen y
        val z = Gensym.gensym(y);
        val fresh_t2 = subst(t2,Var(z),y);
        Let(z,subst(t1,e2,x),subst(fresh_t2,e2,x))
      }
    }
    case LetFun(f, arg, ty, exp1, exp2) => {
      val h = Gensym.gensym(f)
      val y = Gensym.gensym(arg)

      val subExp1 = subst(exp1, Var(y), arg)
      val subExp2 = subst(exp2, Var(h), f)

      LetFun(h, y, ty, subst(subExp1, e2, x), subst(subExp2, e2, x))
    }
    case LetRec(f, arg, argty, ty, exp1, exp2) => {
      val g = Gensym.gensym(f)
      val y = Gensym.gensym(arg)

      val exp1Sub = subst(subst(exp1, Var(g), f), Var(y), arg)
      val exp2Sub = subst(exp2, Var(g), f)

      LetRec(g, y, argty, ty, subst(exp1Sub, e2, x), subst(exp2Sub, e2, x))
    }
    case LetPair(var1, var2, exp1, exp2) => {
      val p1 = Gensym.gensym(var1)
      val p2 = Gensym.gensym(var2)

      LetPair(p1, p2, subst(exp1, e2, x), subst(
        subst(
          subst(exp2, Var(p2), var2),
          Var(p1),
          var1
        ),
        e2,
        x
      ))
    }

    case Pair(expr1, expr2) => Pair(subst(expr1, e2, x), subst(expr2, e2, x))
    case First(e) => First(subst(e, e2, x))
    case Second(e) => Second(subst(e, e2, x))

    // \variable. expr
    case Lambda(variable, varType, expr) => {
      // No need to do much
      if (variable == x) {
        Lambda(variable, varType, subst(expr, e2, x))
      }
      else {
        val f = Gensym.gensym(variable)
        Lambda(f, varType, subst(subst(expr, Var(f), variable), e2, x))
      }
    }
    case Apply(expr1, expr2) =>
      Apply(subst(expr1, e2, x), subst(expr2, e2, x))
    case Rec(g, y, tyy, ty, expr) => {
      val fun = Gensym.gensym(g)
      val arg = Gensym.gensym(y)
      Rec(fun, arg, tyy, ty, subst(
        subst(
          subst(expr, Var(fun), g),
          Var(arg),
          y
        ), e2, x))
    }

    case _ => sys.error(s"[subst] Failed to match an Expr case for ${e1}, forgot to implement a case class?")
  }

  // ======================================================================
  // Exercise 2: Desugaring let fun, let rec and let pair
  // ======================================================================

  def desugar(e: Expr): Expr = e match {
    case Num(n) => Num(n)
    case Plus(e1,e2) => Plus(desugar(e1),desugar(e2))
    case Minus(e1,e2) => Minus(desugar(e1),desugar(e2))
    case Times(e1,e2) => Times(desugar(e1),desugar(e2))

    case Bool(n) => Bool(n)
    case Eq(e1, e2) => Eq(desugar(e1), desugar(e2))
    case IfThenElse(cond, e1, e2) =>
      IfThenElse(desugar(cond), desugar(e1), desugar(e2))

    case Str(s) => Str(s)
    case Length(k) => Length(k)
    case Index(e1, e2) => Index(desugar(e1), desugar(e2))
    case Concat(e1, e2) => Concat(desugar(e1), desugar(e2))

    case Var(v) => Var(v)
    case Let(x, e1, e2) => Let(x, desugar(e1), desugar(e2))
    case LetFun(f, x, xty, e1, e2) => desugar(Let(f, Lambda(x, xty, e1), e2))
    case LetRec(f, x, xty, ty, e1, e2) => desugar(Let(f, Rec(f, x, xty, ty, e1), e2))

    case LetPair(x, y, e1, e2) => {
      val p = Gensym.gensym("p")
      desugar(Let(p, e1, subst(subst(e2, First(Var(p)), x), Second(Var(p)), y)))
    }

    case Pair(e1, e2) => Pair(desugar(e1), desugar(e2))
    case First(e1) => First(desugar(e1))
    case Second(e1) => Second(desugar(e1))

    case Lambda(x, ty, e1) => Lambda(x, ty, desugar(e1))
    case Apply(e1, e2) => Apply(desugar(e1), desugar(e2))
    case Rec(f, x, tyx, ty, e1) => Rec(f, x, tyx, ty, desugar(e1))

    case _ => sys.error("Failed to match Expr type to desugar.")
  }

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

    case Var(x) =>
      if (env contains x) env(x)
      else sys.error(s"[eval] Could not find ${x} in ${env}")

    case Let(x, e1, e2) => eval(env + (x -> eval(env, e1)), e2)
    case Pair(e1, e2) => PairV(eval(env, e1), eval(env, e2))
    case First(e1) => eval(env, e1) match {
      case PairV(v1, v2) => v1
    }
    case Second(e1) => eval(env, e1) match {
      case PairV(v1, v2) => v2
    }


    case Lambda(x, ty, e1) => ClosureV(env, x, e1)
    case Apply(e1, e2) => {
      val v2 = eval(env, e2)
      val v1 = eval(env, e1)

      // println(s"[eval] (Apply) \t${v1}\t\t${v2}")

      v1 match {
        case ClosureV(closureEnv, x, expr) =>
          eval(closureEnv + (x -> v2), expr)
        case RecV(recEnv, f, x, expr) =>
          eval(recEnv + (f -> v1) + (x -> v2), expr)
      }
    }
    case Rec(f, x, tyx, ty, e1) => RecV(env, f, x, e1)

    case _ => sys.error("Failed to match expression " + e + "type to eval.")
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
      case _ => sys.error("non-integer arguments to +")
    }
    case Minus(e1, e2) => (tyOf(ctx, e1), tyOf(ctx, e2)) match {
      case (IntTy, IntTy) => IntTy
      case _ => sys.error("non-integer arguments to -")
    }
    case Times(e1, e2) => (tyOf(ctx, e1), tyOf(ctx, e2)) match {
      case (IntTy, IntTy) => IntTy
      case _ => sys.error("non-integer arguments to *")
    }

    case Bool(n) => BoolTy
    case Eq(e1, e2) => {
      val lhs = tyOf(ctx, e1)
      val rhs = tyOf(ctx, e2)

      val validTypes = List(IntTy, BoolTy, StringTy)
      val isValidType = validTypes.contains(lhs) && validTypes.contains(rhs)
      if (isValidType && lhs == rhs) BoolTy
      else sys.error(s"Types of <lhs> == <rhs> did not match or were not one of ${validTypes}")
    }
    case IfThenElse(conditional, trueBranch, falseBranch) => {
      val isConditionalBool = tyOf(ctx, conditional) == BoolTy
      val trueType = tyOf(ctx, trueBranch)
      val falseType = tyOf(ctx, falseBranch)

      // need if (BoolTy) then <K> else <K>
      if (isConditionalBool && trueType == falseType)
        trueType
      else
        sys.error("IfThenElse needs BoolTy on conditional and matching branch types")
    }

    case Str(s) => StringTy
    case Length(s) => {
      if (tyOf(ctx, s) == StringTy) IntTy
      else sys.error("Length requires a string as argument")
    }
    case Index(s, i) => {
      if (tyOf(ctx, s) == StringTy && tyOf(ctx, i) == IntTy) StringTy
      else sys.error("Index requires a String and Integer and as arguments")
    }
    case Concat(e1, e2) => {
      if (tyOf(ctx, e1) == StringTy && tyOf(ctx, e2) == StringTy) StringTy
      else sys.error("Concat requires two Strings as arguments")
    }


    // Variables and let-binding
    case Var(x) => ctx(x)
    case Let(x, e1, e2) => tyOf(ctx + (x -> (tyOf(ctx, e1))), e2)
    case LetFun(f, x, ty, e1, e2) => {
      val e1Type = tyOf(ctx + (x -> ty), e1)
      tyOf(ctx + (f -> FunTy(ty, e1Type)), e2)
    }
    case LetRec(f, x, xty, ty, e1, e2) => {
      val e1Type = tyOf(ctx + (x -> xty) + (f -> FunTy(xty, ty)), e1)
      tyOf(ctx + (f -> FunTy(xty, ty)), e2)
    }
    case LetPair(x, y , e1, e2) => {
      val e1Type = tyOf(ctx, e1)

      e1Type match {
        case PairTy(t1, t2) => tyOf(ctx + (x -> t1) + (y -> t2), e2)
        case _ => sys.error(s"Failed to match LetPair ${e1Type}")
      }
    }

    case Pair(e1, e2) => PairTy(tyOf(ctx, e1), tyOf(ctx, e2))
    case First(f) => tyOf(ctx, f) match {
      case PairTy(p1, p2) => p1
    }
    case Second(f) => tyOf(ctx, f) match {
      case PairTy(p1, p2) => p2
    }

    case Lambda(x, ty, e) => tyOf(ctx + (x -> ty), e)
    case Apply(e1, e2) => {
      val e2Type = tyOf(ctx, e2)

      val e1Type = tyOf(ctx, e1)

      e1Type match {

        case FunTy(argType, funcOutType) => {
          if (argType == e2Type) funcOutType
          else {
            sys.error("Apply requires the argument to match the abstraction function")
          }
        }
        case _ => {
          e2Type
        }
      }
    }

    case Rec(f, x, tyx, ty, e) => tyOf(ctx + (x -> tyx) + (f -> FunTy(tyx, ty)), e)

    case _ => sys.error(s"[tyOf] Failed to match expression ${e} in context ${ctx}")
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
    let fun sameLastChar(input: str * str) =
      let (s1,s2) = input in
      index(s1,length(s1)-1) == index(s2,length(s2)-1)
    in sameLastChar("abcz","abcdefghijklmnopqrstuvwxyz")
    """)


  // ======================================================================
  // Exercise 6: Fibonacci sequence
  // ======================================================================

  def fib: Expr = parser.parseStr("""
    let rec fib(n:int):int =
      if (n == 1) then 1
      else if (n == 2) then 1
      else (fib(n - 2) + fib(n - 1))
    in fib
  """)


  // ======================================================================
  // Exercise 7: Substrings
  // ======================================================================

  def substring: Expr = parser.parseStr("""
    let fun subs(input:str * str) =

      let (substring, string) = input in

        let rec match(counts: int * int): int * int =
          let (x, y) = counts in
            if (x == length(substring)) then (x, y)
            else if (y == length(string)) then (x, y)
            else if (index(substring, x) == index(string, y)) then match (x + 1, y + 1)
            else match (0, y + 1)

        in fst(match (0, 0)) == length(substring)

    in subs
  """)


  /*======================================================================
   The rest of this file is support code, which you should not (and do not
   need to) change.
   ====================================================================== */

  class CWParser extends StandardTokenParsers with PackratParsers {

    type P[+A] = PackratParser[A]

    def parseStr(input: String): Expr = {
      phrase(expression)(new lexical.Scanner(input)) match {
        case Success(ast, _) => ast
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

      try {
        print("Type Checking...");
        val ty = typecheck(ast);
        println("Done!");
        println("Type of Expression: " + ty.toString + "\n") ;
      } catch {
          case e:Throwable => println("Error: " + e)
      }
      println("Desugaring...");
      val core_ast = desugar(ast);
      println("Done!");
      println("Desugared AST: " + core_ast.toString + "\n") ;

      println("Evaluating...");
      println("Result: " + evaluate(core_ast))


    }

    def start(): Unit = {
      println("Welcome to Giraffe! (V1.8, October 16, 2015)");
      println("Enter expressions to evaluate, :load <filename.gir> to load a file, or :quit to quit.");
      repl()
    }

    def repl(): Unit = {
      print("Giraffe> ");
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


