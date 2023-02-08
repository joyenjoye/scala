# Types and Pattern

- [Decomposition](#decomposition)
  - [Exercise](#exercise)
  - [Type Tests and Type Casts](#type-tests-and-type-casts)
  - [Object-Oriented Decomposition](#object-oriented-decomposition)
  - [Pattern Matching](#pattern-matching)
- [List](#list)
  - [Operations on Lists](#operations-on-lists)
  - [List Patterns](#list-patterns)
  - [Exercise](#exercise-1)
  - [Sorting Lists](#sorting-lists)
- [Enumeration](#enumeration)
  - [Enums for ADTs](#enums-for-adts)
  - [Pattern Matching on ADTs](#pattern-matching-on-adts)
  - [Simple Enums](#simple-enums)
  - [Parameterized enums](#parameterized-enums)
  - [Domain Modeling](#domain-modeling)


## Decomposition

Suppose you want to write a small interpreter for arithmetic expressions. To keep it simple, let’s restrict ourselves to numbers and additions. Expressions can be represented as a class hierarchy, with a base trait `Expr`
and two subclasses, `Number` and `Sum`. To treat an expression, it’s necessary to know the expression’s shape and
its components. This brings us to the following implementation

```scala
trait Expr:
  def isNumber: Boolean
  def isSum: Boolean
  def numValue: Int
  def leftOp: Expr
  def rightOp: Expr

class Number(n: Int) extends Expr:
  def isNumber = true
  def isSum = false
  def numValue = n
  def leftOp = throw Error("Number.leftOp")
  def rightOp = throw Error("Number.rightOp")

class Sum(e1: Expr, e2: Expr) extends Expr:
  def isNumber = false
  def isSum = true
  def numValue = throw Error("Sum.numValue")
  def leftOp = e1
  def rightOp = e2
```

You can now write an evaluation function as follows.


```scala
def  eval(e: Expr): Int =
  if e.isNumber then e.numValue
  else if e.isSum then eval(e.leftOp) + eval(e.rightOp)
  else throw Error("Unknown expression" + e)
```

Problems: 

- Writing all these classification and accessor functions quickly becomes tedious!
- There’s no static guarantee you use the right accessor functions. You might hit an Error case if you are not careful.
  
### Exercise


To integrate `Prod` and `Var` into Hierarchy, how many new method do we need? 


### Type Tests and Type Casts

A "hacky" solution could use type tests and type casts. Scala let’s you do these using methods defined in class Any:

```scala
def isInstanceOf[T]: Boolean // checks whether this object’s type conforms to
def asInstanceOf[T]: T // treats this object as an instance of type ‘T‘// throws ‘ClassCastException‘ if it isn’t.
```

These correspond to Java’s type tests and casts

|Scala | Java|
| --- | --- |
x.isInstanceOf[T] | x instanceof T
x.asInstanceOf[T] | (T) x

But their use in Scala is discouraged, because there are better alternatives.
Here’s a formulation of the eval method using type tests and casts:

```scala
def eval(e: Expr): Int =
  if e.isInstanceOf[Number] then
    e.asInstanceOf[Number].numValue
  else if e.isInstanceOf[Sum] then
    eval(e.asInstanceOf[Sum].leftOp)
    + eval(e.asInstanceOf[Sum].rightOp)
  else throw Error(”Unknown expression ” + e)
```
This is ugly and potentially unsafe.

### Object-Oriented Decomposition

Suppose that all you want to do is evaluate expressions. You could then define:

```scala
trait Expr:
  def eval: Int
class Number(n: Int) extends Expr:
  def eval: Int = n
class Sum(e1: Expr, e2: Expr) extends Expr:
  def eval: Int = e1.eval + e2.eval
```
But what happens if you’d like to display expressions now? You have to define new methods in all the subclasses.

OO decomposition mixes data with operations on the data. This can be the right thing if there’s a need for encapsulation and data abstraction. On the other hand, it increases complexity(*) and adds new dependencies to classes. It makes it easy to add new kinds of data but hard to add new kinds of operations.

### Pattern Matching

A case class definition is similar to a normal class definition, except that it is preceded by the modifier `case`. For example:

```scala
trait Expr
case class Number(n: Int) extends Expr
case class Sum(e1: Expr, e2: Expr) extends Expr
```

Like before, this defines a trait `Expr`, and two concrete subclasses `Number` and `Sum`. However, these classes are now empty. So how can we access the members?

Pattern matching is a generalization of switch from C/Java to class hierarchies. It’s expressed in Scala using the keyword match.

```scala
def eval(e: Expr): Int = e match
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
```
The syntax of pattern matching is as follows:

- `match` is preceded by a selector expression and is followed by a sequence of cases, pat => expr.
- Each `case` associates an expression expr with a pattern pat.
- A `MatchError` exception is thrown if no pattern matches the value of the selector.

Patterns are constructed from:
- constructors, e.g. Number, Sum,
- variables, e.g. n, e1, e2,
- wildcard patterns _,
- constants, e.g. 1, true.
- type tests, e.g. n: Number
  
Variables always begin with a lowercase letter. The same variable name can only appear once in a pattern. So, `Sum(x, x)` is not a legal pattern. Names of constants begin with a capital letter, with the exception of the reserved words `null`, `true`, `false`.

## List

The list is a fundamental data structure in functional programming.

```scala
val fruit = List(”apples”, ”oranges”, ”pears”)
val nums = List(1, 2, 3, 4)
val diag3 = List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1))
val empty = List()
```
There are two important differences between lists and arrays.
- Lists are immutable — the elements of a list cannot be changed.
- Lists are recursive, while arrays are flat.

Like arrays, lists are homogeneous: the elements of a list must all have the same type.
The type of a list with elements of type T is written `scala.List[T]` or shorter just `List[T]`

```scala
val fruit: List[String] = List(”apples”, ”oranges”, ”pears”)
val nums : List[Int] = List(1, 2, 3, 4)
val diag3: List[List[Int]] = List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1))
val empty: List[Nothing] = List()
```

All lists are constructed from:
- the empty list `Nil`, and
- the construction operation `::` (pronounced cons):
  
`x :: xs` gives a new list with the first element x, followed by the elements of xs.

Convention: Operators ending in ":" associate to the right. `A :: B :: C` is interpreted as `A :: (B :: C)`. We can thus omit the parentheses in the definition above.

```scala
val nums = 1 :: 2 :: 3 :: 4 :: Nil
```
### Operations on Lists

All operations on lists can be expressed in terms of the following three: 
- head the first element of the list
- tail the list composed of all the elements except the first.
- isEmpty ‘true‘ if the list is empty, ‘false‘ otherwise.

These operations are defined as methods of objects of type List. 

```scala
fruit.head == ”apples”
fruit.tail.head == ”oranges”
diag3.head == List(1, 0, 0)
empty.head == throw NoSuchElementException(”head of empty list”)
```
### List Patterns

It is also possible to decompose lists with pattern matching.
|  Pattern   |   Description  |
| --- | --- |
  `Nil` | The `Nil` constant
`p :: ps` | A pattern that matches a list with a head matching `p` and a tail matching `ps`.
`List(p1, ..., pn)` | same as `p1 :: ... :: pn :: Nil`

### Exercise

Consider the pattern `x :: y :: List(xs, ys) :: zs`. What is the condition that describes most accurately the length L of the
lists it matches?

- [ ] L == 3
- [ ] L == 4
- [ ] L == 5
- [x] L >= 3
- [ ] L >= 4
- [ ] L >= 5

### Sorting Lists

Suppose we want to sort a list of numbers in ascending order:

- One way to sort the list List(7, 3, 9, 2) is to sort the tail List(3, 9, 2) to obtain List(2, 3, 9).
- The next step is to insert the head 7 in the right place to obtain the result List(2, 3, 7, 9).

This idea describes *Insertion Sort* :

```scala
def isort(xs: List[Int]): List[Int] = xs match
  case List() => List()
  case y :: ys => insert(y, isort(ys))

def insert(x: Int, xs: List[Int]): List[Int] = xs match
  case List() => List(x)
  case y :: ys =>
  if x < y then x :: xs else y :: insert(x, ys)
```

What is the worst-case complexity of insertion sort relative to the length
of the input list N?

- [ ] the sort takes constant time
- [x] proportional to N
- [ ] proportional to N * log(N)
- [ ] proportional to N * N


## Enumeration
In the previous sessions, you have learned how to model data with class hierarchies. Classes are essentially bundles of functions operating on some common values represented as fields. They are a very useful abstraction, since they allow encapsulation of data. 

But sometimes we just need to compose and decompose pure data without any associated functions. Case classes and pattern matching work well for this task.

Here’s our case class hierarchy for expressions again:

```scala
trait Expr
object Expr:
  case class Var(s: String) extends Expr
  case class Number(n: Int) extends Expr
  case class Sum(e1: Expr, e2: Expr) extends Expr
  case class Prod(e1: Expr, e2: Expr) extends Expr
```
This time we have put all case classes in the Expr companion object, in
order not to pollute the global namespace. So it’s `Expr.Number(1)` instead of `Number(1)`, for example. One can still "pull out" all the cases using an import.

```scala
import Expr.*
```

Pure data definitions like these are called algebraic data types, or ADTs
for short. They are very common in functional programming. To make them even more convenient, Scala offers some special syntax.

### Enums for ADTs

An `enum` enumerates all the cases of an ADT and nothing else.

```scala
enum Expr:
  case Var(s: String)
  case Number(n: Int)
  case Sum(e1: Expr, e2: Expr)
  case Prod(e1: Expr, e2: Expr)
```

This `enum` is equivalent to the case class hierarchy on the previous slide, but is shorter, since it avoids the repetitive `class ... extends Expr` notation.

### Pattern Matching on ADTs

Match expressions can be used on enums as usual. For instance, to print expressions with proper parameterization:

```scala
def show(e: Expr): String = e match
  case Expr.Var(x) => x
  case Expr.Number(n) => n.toString
  case Expr.Sum(a, b) => s”${show(a)} + ${show(a)}}”
  case Expr.Prod(a, b) => s”${showP(a)} * ${showP(a)}”
def showP(e: Expr): String = e match
  case e: Sum => s”(${show(expr)})”
  case _ => show(expr)
```

### Simple Enums

Cases of an enum can also be simple values, without any parameters.

For example, define a Color type with values Red, Green, and Blue:

```scala
enum Color:
  case Red
  case Green
  case Blue
```
We can also combine several simple cases in one list:

```scala
enum Color:
case Red, Green, Blue
```

For pattern matching, simple cases count as constants:

```scala
enum DayOfWeek:
  case Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
import DayOfWeek.*
def isWeekend(day: DayOfWeek) = day match
  case Saturday | Sunday => true
  case _ => false
```

### Parameterized enums

Enums can be parameterized.

- Enumeration cases that pass parameters have to use an explicit
`extends` clause. 
- The expression `e.ordinal` gives the ordinal value of the enum case `e`. Cases start with zero and are numbered consecutively.
- `values` is an immutable array in the companion object of an enum
that contains all enum values.
- Only simple cases have ordinal numbers and show up in values,
parameterized cases do not.

```scala
enum Direction(val dx: Int, val dy: Int):
  case Right extends Direction( 1, 0)
  case Up extends Direction( 0, 1)
  case Left extends Direction(-1, 0)
  case Down extends Direction( 0, -1)
  def leftTurn = Direction.values((ordinal + 1) % 4)
end Direction

val r = Direction.Down
val u = r.leftTurn 
val v = (u.dx, u.dy)
val x = Direction.Right.ordinal
val y = Direction.Up.ordinal
val z = Direction.Left.ordinal
```

The Direction `enum` is expanded by the Scala compiler to roughly the
following structure:

```scala
abstract class Direction(val dx: Int, val dy: Int):
   def rightTurn = Direction.values((ordinal - 1) % 4)
object Direction:
  val Right = new Direction( 1, 0) {}
  val Up = new Direction( 0, 1) {}
  val Left = new Direction(-1, 0) {}
  val Down = new Direction( 0, -1) {}
end Direction
```
There are also compiler-defined helper methods `ordinal` in the class and
`values` and `valueOf` in the companion object.

### Domain Modeling

ADTs and enums are particularly useful for domain modelling tasks where one needs to define a large number of data types without attaching operations. For example, modelling payment methods:

```scala
enum PaymentMethod:
  case CreditCard(kind: Card, holder: String, number: Long, expires: Date)
  case PayPal(email: String)
  case Cash

enum Card:
  case Visa, Mastercard, Amex
```

In this section, two uses of enum definitions are covered:
-  as a shorthand for hierarchies of case classes,
-  as a way to define data types accepting alternative values,
The two cases can be combined: an enum can comprise parameterized and simple cases at the same time. Enums are typically used for pure data, where all operations on such data are defined elsewhere.
