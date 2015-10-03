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
  val m = n - 1
  if (n == 0) {1} else {n * factorial2(m)}
}


def factorial3(n: Int): Int = { val m = n-1;
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
def cycle(x:Int,y:Int,z:Int): (Int,Int,Int) = (y, z, x)


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
case class Black() extends Colour

/* Exercise 5 */
def favouriteColour(c: Colour): Boolean = c match {
  case Red() => false;
  case Blue() => true;
  case Green() => false;
  case Black() => true;
}


abstract class Shape
case class Circle(r: Double, x: Double, y: Double) extends Shape
case class Rectangle(llx: Double, lly: Double, w:Double, h:Double) extends Shape

def center(s: Shape): (Double,Double) = s match {
  case Rectangle(llx,lly,w,h) => (llx+w/2, lly+h/2)
  case Circle(r,x,y) => (x,y)
}

/* Exercise 6 */
def boundingBox(s: Shape): Shape = s match {
  case Rectangle(llx, lly, w, h) => s.asInstanceOf[Rectangle]
  case Circle(r, x, y) => Rectangle(x - r, y - r, 2 * r, 2 * r)
}

/* Exercise 7 */
def mayOverlap(s1: Shape, s2: Shape) = {
  val bb1 = boundingBox(s1)
  val bb2 = boundingBox(s2)


}



/* Part 5 */

val anonIncr = {x: Int => x+1} // anonymous version of incr
val anonAdd = {x: Int => {y: Int => x + y}}

/* Exercise 8 */
def compose[A, B, C](f: A => B, g: B => C) = sys.error("todo")

/* Exercise 9 */
def e1 = sys.error("todo")
def e2 = sys.error("todo")

def isEmpty[A](l: List[A]) = l match {
  case Nil => true
  case x :: y => false
}


/* Exercise 10 */
def map[A, B](f: A => B, l: List[A]): List[B] = sys.error("todo")

/* Exercise 11 */
def filter[A, B](f: A => Boolean, l: List[A]): List[B] = sys.error("todo")

/* Exercise 12 */
def reverse[A](l: List[A]): List[A] = sys.error("todo")


/* Part 6 */

def empty[K,V]: List[(K,V)] = List()

/* Exercise 13 */
def lookup[K, V](m: List[(K, V)], k: K): V = sys.error("todo")

/* Exercise 14 */
def update[K, V](m: List[(K, V)], k: K, v: V): List[(K, V)] = sys.error("todo")

/* Exercise 15 */
def keys[K,V](m: List[(K,V)]): List[K] = sys.error("todo")

/* Exercise 16 */
val presidentListMap = ListMap() // TODO

/* Exercise 17 */
def m0_withUpdate = sys.error("todo")

/* Exercise 18 */
def list2map[K,V](l: List[(K,V)]): ListMap[K,V] = sys.error("todo")