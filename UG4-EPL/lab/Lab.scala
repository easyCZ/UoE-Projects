import scala.collection.immutable.ListMap

/* Part 2 */

def incr(x: Int): Int = x + 1
def double(x: Int): Int = x + x
def square(x: Int): Int = x * x

def factorial(n: Int): Int =
  if (n == 0) {1} else {n * factorial(n-1)}

def power(x: Int, n: Int): Int =
  if (n == 0) {1} else {x * power(x,n-1)}

def factorial1(n: Int): Int = {
  val m = n-1 ; if (n == 0) {1} else {n * factorial1(m)}
}


def factorial2(n: Int): Int = {
  val m = n-1;
  if (n == 0) {1} else {n * factorial2(m)}
}


def factorial3(n: Int): Int = {
  val m = n-1;
  if (n == 0) {
    return 1;
  } else {
    return n * factorial3(m);
  }
}

/* Exercise 1 */
def p(x: Int, y:Int): Int =
  square(x) + 2 * x * y + power(y, 3) - 1

/* Exercise 2 */
def sum(n: Int): Int =
  if (n == 0) 0 else n + sum(n-1)

/* Part 3 */

/* Exercise 3 */
def cycle(q:(Int,Int,Int)): (Int,Int,Int) =
  (q._2, q._3, q._1)

/* Part 4 */


def nameFromNum(presidentNum: Int): String = presidentNum match {
  case 41 => "George H. W. Bush"
  case 42 => "Bill Clinton"
  case 43 => "George W. Bush"
  case 44 => "Barack Obama"
}

def numFromName(presidentName: String): Int = presidentName match {
  case "George H. W. Bush" => 41
  case "Bill Clinton" => 42
  case "George W. Bush" => 43
  case "Barack Obama" => 44
}

/* Exercise 4 */
def suffix(n: Int): String = n match {
  case 11 | 12 | 13 => n + "th"
  case _ => n + n % 10 match {
    case 1 => "st"
    case 2 => "nd"
    case 3 => "rd"
    case _ => "th"
  }
}


abstract class Colour
case class Red() extends Colour
case class Green() extends Colour
case class Blue() extends Colour

/* Exercise 5 */
def favouriteColour(c: Colour): Boolean = c match {
  case Red() => false;
  case Blue() => true;
  case Green() => false;
}


abstract class Shape
case class Circle(r: Double, x: Double, y: Double) extends Shape
case class Rectangle(llx: Double, lly: Double, w:Double, h:Double) extends Shape

def center(s: Shape): (Double,Double) = s match {
  case Rectangle(llx,lly,w,h) => (llx+w/2, lly+h/2)
  case Circle(r,x,y) => (x,y)
}

/* Exercise 6 */
def boundingBox(s: Shape): Rectangle = s match {
  case Rectangle(llx, lly, w, h) => Rectangle(llx, lly, w, h)
  case Circle(r, x, y) => Rectangle(x - r, y - r, 2 * r, 2 * r)
}

/* Exercise 7 */
def mayOverlap(s1: Shape, s2: Shape): Boolean = (s1, s2) match {
  case (Circle(_, _, _), _) => mayOverlap(boundingBox(s1), s2)
  case (_, Circle(_, _, _)) => mayOverlap(s1, boundingBox(s2))
  case (Rectangle(llx1, lly1, w1, h1), Rectangle(llx2, lly2, w2, h2)) => {

    val minX: Double = math.min(llx1, llx2)
    val minY: Double = math.min(lly1, lly2)
    val width: Double = math.max(llx1, llx2) - minX
    val height: Double = math.max(lly1, lly2) - minY

    return w1 + w2 > width || h1 + h2 > height
  }
}



/* Part 5 */

val anonIncr = {x: Int => x+1} // anonymous version of incr
val anonAdd = {x: Int => {y: Int => x + y}}

/* Exercise 8 */
def compose[A, B, C](f: A => B, g: B => C) = {
  a: A => g(f(a))
}

/* Exercise 9 */
def e1 = {x: Int => "a" * x }
def e2 = {y: String => y.length % 2 == 0 }

// scala> compose(e1, e2)(2)
// res804: Boolean = true

// scala> compose(e1, e2)(1)
// res805: Boolean = false

// scala> compose(e1, e2)
// res802: Int => Boolean = <function1>

// scala> compose(e2, e1)
// <console>:43: error: type mismatch;
//  found   : Int => String
//  required: Boolean => String
//        compose(e2, e1)

def isEmpty[A](l: List[A]) = l match {
  case Nil => true
  case x :: y => false
}

def isEven = {x: Int => x % 2 == 0}


/* Exercise 10 */
def map[A, B](f: A => B, l: List[A]): List[B] = l match {
  case Nil => List[B]()
  case x :: xs => f(x) :: map(f, xs)
}

/* Exercise 11 */
def filter[A](f: A => Boolean, l: List[A]): List[A] = l match {
  case Nil => List[A]()
  case x :: xs => f(x) match {
    case true => x :: filter(f, xs)
    case _ => filter(f, xs)
  }
}

/* Exercise 12 */
def reverse[A](l: List[A]): List[A] = l match {
  case Nil => List[A]()
  case x :: xs => reverse(xs) :+ x
}


/* Part 6 */

def empty[K,V]: List[(K,V)] = List()

/* Exercise 13 */
def lookup[K, V](m: List[(K, V)], k: K): V = m match {
  case (`k`, value) :: _ => value
  case x :: xs => lookup(xs, k)
  case _ => sys.error("not found")
}

/* Exercise 14 */
def update[K, V](m: List[(K, V)], k: K, v: V): List[(K, V)] = m match {
  case Nil => List[(K, V)]()
  case (`k`, value) :: xs => (k, v) :: xs
  case x :: xs => x :: update(xs, k, v)
}

/* Exercise 15 */
def keys[K,V](m: List[(K,V)]): List[K] = m match {
  case Nil => List[K]()
  case (key, value) :: xs => key :: keys(xs)
}

/* Exercise 16 */
val presidentListMap = ListMap(
  41 -> "George H. W. Bush",
  42 -> "Bill Clinton",
  43 -> "George W. Bush",
  44 -> "Barack Obama"
)

/* Exercise 17 */
def m0_withUpdate = ListMap[Int, String]() + (1 -> "a") + (2 -> "b")

/* Exercise 18 */
def list2map[K,V](l: List[(K,V)]): ListMap[K,V] =
  l.foldLeft(ListMap[K, V]()) ((listMap, tuple) => listMap + (tuple._1 -> tuple._2))
