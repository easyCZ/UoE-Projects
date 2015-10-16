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
  def subst(e1:Expr, e2:Expr, x: Variable): Expr = {
    println("<subst>" + e1 + " | " + e2+ " | " + x)
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
          val out = Let(z, subst(t1, e2, x), subst(fresh_t2, e2, x))
          println("LET SUB: out: " + out)
          out
        }
      // let fun g(x) = 2 * x in g(x) * z(k) * x  =>
      //    let g_1(x_1) = 2 * x_1 in g_1(x) * z(k) * x
      case LetFun(f, arg, ty, t1, t2) => {
        val funName = Gensym.gensym(f)
        val argName = Gensym.gensym(arg)

        println(s"LetFun: func\t${f} -> ${funName}")
        println(s"LetFun: args\t${arg} -> ${argName}")

        // Rename arguments
        val t1Sub = subst(t1, Var(argName), arg)
        // Rename function name
        val t2Sub = subst(t2, Var(funName), f)

        LetFun(funName, argName, ty, t1Sub, t2Sub)
      }
      case LetRec(f, arg, xty, ty, t1, t2) => {
        val fNew = Gensym.gensym(f)
        LetRec(fNew, arg, xty, ty, t1, subst(t2, e2, fNew))
      }
      case LetPair(var1, var2, exp1, exp2) => {
        val v1 = Gensym.gensym(var1)
        val v2 = Gensym.gensym(var2)

        LetPair(v1, v2, exp1, subst(subst(exp2, e2, v2), e2, v1))
      }

      case Pair(expr1, expr2) => Pair(subst(expr1, e2, x), subst(expr2, e2, x))
      case First(e) => First(subst(e, e2, x))
      case Second(e) => Second(subst(e, e2, x))

      // \variable. expr
      case Lambda(variable, varType, expr) => {
        if (variable == x) Lambda(variable, varType, subst(expr, e2, x))
        else {
          val freshArg = Gensym.gensym(variable)
          val updatedLambda = subst(expr, Var(freshArg), variable)

          Lambda(freshArg, varType, subst(updatedLambda, e2, x))
        }
      }
      case Apply(expr1, expr2) =>
        Apply(subst(expr1, e2, x), subst(expr2, e2, x))

      // rec f(argName: tyx): ty = expr
      case Rec(funcName, argName, tyx, ty, expr) => {
          val newFuncName = Gensym.gensym(funcName)
          val newFuncArg = Gensym.gensym(argName)

          // Update expr with new name
          val newFuncExpr = subst(expr, Var(newFuncName), funcName)
          val newArgExpr = subst(newFuncExpr, Var(newFuncArg), argName)

          Rec(newFuncName, newFuncArg, tyx, ty, subst(newArgExpr, e2, x))
      }

      case _ => sys.error("Failed to match an Expr case, forgot to implement a case class?")
    }
  }


  // ======================================================================
  // Exercise 2: Desugaring let fun, let rec and let pair
  // ======================================================================

  def desugar(e: Expr): Expr = {

    println(s"[desugar]\t${e}")

    var test = e match {

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
      // let fun f(argVar) = e1 in e2  =>  let f = \argVar. e1 in e2
      case LetFun(funcName, argVar, argType, e1, e2) => {

        Let(funcName, Lambda(argVar, argType, e1), e2)
      }
      // let rec f(argVar) = e1 in e2  =>  let f = rec f(argVar) e1 in e2
      case LetRec(funcName, argVar, argType, recType, e1, e2) =>
        Let(funcName, Rec(funcName, argVar, argType, recType, desugar(e1)), desugar(e2))
      // Let(x: Variable, e1: Expr, e2: Expr)
      // LetPair(x: Variable,y: Variable, e1:Expr, e2:Expr)
      case LetPair(varX, varY, pairExpression, e2) => {
        // println("Desugarig LETPAIR")
        val p = Gensym.gensym("p")

        val desugaredPair = desugar(pairExpression)

        val substPforX = subst(e2, First(Var(p)), varX)
        val substPforY = subst(substPforX, Second(Var(p)), varY)

        // println("e2: " + e2)
        // println("Subst for X: " + substPforX)
        // println("Subst for Y: " + substPforY)
        Let(p, desugaredPair, desugar(substPforY))
      }

      case Pair(e1, e2) => Pair(desugar(e1), desugar(e2))
      case First(e1) => First(desugar(e1))
      case Second(e1) => Second(desugar(e1))

      case Lambda(x, ty, e1) => Lambda(x, ty, desugar(e1))
      case Apply(e1, e2) => Apply(desugar(e1), desugar(e2))
      case Rec(f, x, tyx, ty, e1) => Rec(f, x, tyx, ty, desugar(e1))

      case _ => sys.error("Failed to match Expr type to desugar.")
    }

    println(s"[desugar] \t${test}")
    println()
    test
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

  def eval (env: Env[Value], e: Expr): Value = {
    println()
    println("[eval] exp:\t" + e)
    println("[eval] env:\t" + env)


    e match {
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

      case Var(x) => {
        if (env contains x) {
          env(x) match {
            case ClosureV(environment, variable, expression) =>
              println("Closure Expr: " + expression)
              eval(environment, subst(expression, Var(variable), variable))
            case a => a
          }
        }
        else sys.error("eval: Key " + x + " not found in environment. env=" + env)
      }
      case Let(x, e1, e2) => {

        val v1 = eval(env, e1)
        val env2 = env + (x -> v1)

        println(s"[eval] (Let) updated env: \t ${env2}")
        eval(env2, e2)
      }

      case Pair(e1, e2) => PairV(eval(env, e1), eval(env, e2))
      // case First(e1) => e1 match {
      //   case Pair(p1, p2) => eval(env, p1)
      //   case Var(x) => eval(env, e1)
      // }
      case First(Pair(a, b)) => eval(env, a)
      case First(Var(a)) => env(a) match {
        case PairV(m, n) => m
      }
      case Second(Pair(a, b)) => eval(env, b)
      case Second(Var(b)) => env(b) match {
        case PairV(m, n) => n
      }

      // TODO: Functions
      case Lambda(x, ty, e) => ClosureV(env, x, Lambda(x, ty, e))
      case Apply(e1, e2) => {
        println(s"[eval](Apply) ${e1} ${e2}")
        val expVal = eval(env, e2)

        println(s"Apply E1: ${e1}")
        e1 match {

          case Lambda(x, ty, lambdaExpr) => {
            // Update env with result of expVal
            val environment = env + (x -> expVal)
            eval(environment, lambdaExpr)
          }

          case Rec(funName, argName, typeOfX, ty, recExpr) => {
            println(s"[eval](Apply Rec) ${e1}")
            // Update env
            val env1 = env + (argName -> expVal)
            val recVal = RecV(env1, funName, argName, recExpr)
            val env2 = env1 + (funName -> recVal)

            eval(env2, recExpr)
          }

          case _ => eval(env, e1)

          // case _ => sys.error("Failed to match application rule for " + e1)
        }

      }
      case Rec(f, x, tyx, ty, e) => ClosureV(env, x, e)

      // case LetFun(f, arg, ty, e1, e2) => {
      //   val desugared = desugar(e)
      //   println(s"[eval] (LetFun) desugared: ${desugared}")
      //   eval(env, desugared)
      // }
      //   // sys.error("eval: LetFun should have been desugared.")
      // case LetRec(f, arg, xty, ty, e1, e2) => eval(env, desugar(e))
      //   // eval()
      //   // sys.error("eval: LetRec should have been desugared.")
      // case LetPair(x, y, e1, e2) => eval(env, desugar(e))
        // sys.error("eval: LetPair should have been desugared.")

      case _ => sys.error("Failed to match expression " + e + "type to eval.")
    }
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

      tyOf(ctx, e1) match {

        case FunTy(argType, funcOutType) => {
          if (argType == e2Type) funcOutType
          else {
            sys.error("Apply requires the argument to match the abstraction function")
          }
        }

        case _ => sys.error("Apply failed to match FunTy, this shouldn't be happening really.")
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
    let fun sameLastChar(input:(str,str)) =
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
  // let rec fib(n:int):int = if (n == 1) then 1 else if (n == 2) then 1 else (fib(n - 2) + fib(n - 1)) in fib


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



