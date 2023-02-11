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

## Subtyping and Generics

Consider the method `assertAllPos` which
- takes an `IntSet`
- returns the `IntSet` itself if all its elements are positive
- throws an `exception` otherwise
  
What would be the best type you can give to `assertAllPos`? Maybe:

```scala
def assertAllPos(s: IntSet): IntSet
```

In most situations this is fine, but can one be more precise?

### Type Bounds

One might want to express that assertAllPos takes `Empty` sets to `Empty` sets and `NonEmpty` sets to `NonEmpty` sets. A way to express this is:

```scala
def assertAllPos[S <: IntSet](r: S): S = ...
```

Here, `<: IntSet` is an upper bound of the type parameter `S`:

It means that `S` can be instantiated only to types that conform to `IntSet`. Generally, the notation
- `S <: T` means: `S` is a subtype of `T`, and
- `S >: T` means: `S` is a supertype of `T`, or `T` is a subtype of `S`.


You can also use a lower bound for a type variable.

`[S >: NonEmpty]` introduces a type parameter S that can range only over supertypes of `NonEmpty`. So S could be one of `NonEmpty`, `IntSet`, `AnyRef`, or `Any`. We will see in the next session examples where lower bounds are useful.

Finally, it is also possible to mix a lower bound with an upper bound. `[S >: NonEmpty <: IntSet]` would restrict S any type on the interval between NonEmpty and IntSet.

### Covariance

There’s another interaction between subtyping and type parameters we need to consider. Given `NonEmpty <: IntSet`, whether or not `List[NonEmpty] <: List[IntSet]`

Intuitively, this makes sense: A list of non-empty sets is a special case of a list of arbitrary sets. We call types for which this relationship holds covariant because their subtyping relationship varies with the type parameter. Does covariance make sense for all types, not just for List?

### Array Typing Problem

For perspective, let’s look at arrays in Java (and C#).

- An array of T elements is written T[] in Java.
- In Scala we use parameterized type syntax Array[T] to refer to the same type.
  
Arrays in Java are covariant, so one would have:

```java
NonEmpty[] <: IntSet[]
```

But covariant array typing causes problems. To see why, consider the Java code below.

```java
NonEmpty [] a = new NonEmpty []{
new NonEmpty (1 , new Empty () , new Empty ())};
IntSet [] b = a ;
b [0] = new Empty ();
NonEmpty s = a [0];
```

It looks like we assigned in the last line an Empty set to a variable of type NonEmpty! What went wrong?


### The Liskov Substitution Principle

The following principle, stated by Barbara Liskov, tells us when a type can be a subtype of another. If `A <: B`, then everything one can to do with a value of type `B` one should also be able to do with a value of type `A`.

[The actual definition Liskov used is a bit more formal. It says: Let `q(x)` be a property provable about objects `x` of type `B`. Then `q(y)` should be provable for objects `y` of type `A` where `A <: B`.

### Exercise

The problematic array example would be written as follows in Scala:

```scala
val a: Array[NonEmpty] = Array(NonEmpty(1, Empty(), Empty()))
val b: Array[IntSet] = a
b(0) = Empty()
val s: NonEmpty = a(0)
```
When you try out this example, what do you observe?

- [ ] A type error in line 1
- [x] A type error in line 2
- [ ] A type error in line 3
- [ ] A type error in line 4
- [ ] A program that compiles and throws an exception at run-time
- [ ] A program that compiles and runs without exception


##  Variance

You have seen the the previous session that some types should be covariant whereas others should not. Roughly speaking, a type that accepts mutations of its elements should not be covariant. But immutable types can be covariant, if some conditions on methods are met.

Say `C[T]` is a parameterized type and `A`, `B` are types such that `A <: B`. In general, there are three possible relationships between `C[A]` and `C[B]`:
- `C[A] <: C[B]` `C` is covariant
- `C[A] >: C[B]` `C` is contravariant
- neither `C[A]` nor `C[B]` is a subtype of the other `C` is nonvariant

Scala lets you declare the variance of a type by annotating the type parameter:
- class `C[+A] { ... }` `C` is covariant
- class `C[-A] { ... }` `C` is contravariant
- class `C[A] { ... }` `C` is nonvariant

### Exercise 

Assume the following type hierarchy and two function types:

```scala
trait Fruit
class Apple extends Fruit
class Orange extends Fruit
type FtoO = Fruit => Orange
type AtoF = Apple => Fruit
```
According to the Liskov Substitution Principle, which of the following should be true?

- [x] `FtoO <: AtoF`
- [ ] `AtoF <: FtoO`
- [ ] `A` and `B` are unrelated.
 

Generally, we have the following rule for subtyping between function types: 

If `A2 <: A1` and `B1 <: B2`, then `A1 => B1 <: A2 => B2`

So functions are contravariant in their argument type(s) and covariant in their result type. This leads to the following revised definition of the Function1 trait: 

```scala
package scala
trait Function1[-T, +U]:
  def apply(x: T): U
```

### Variance Check

We have seen in the array example that the combination of covariance with certain operations is unsound.
In this case the problematic operation was the update operation on an array. If we turn Array into a class, and update into a method, it would look like this:

```scala
class Array[+T]:
  def update(x: T) = ...
```

The problematic combination is
-  the covariant type parameter T
-  which appears in parameter position of the method update.


The Scala compiler will check that there are no problematic combinations when compiling a class with variance annotations. Roughly,

- covariant type parameters can only appear in method results.
- contravariant type parameters can only appear in method parameters.
- invariant type parameters can appear anywhere.

The precise rules are a bit more involved, fortunately the Scala compiler performs them for us.

Let’s have a look again at Function1:

```scala
trait Function1[-T, +U]:
  def apply(x: T): U
```

Here,
- T is contravariant and appears only as a method parameter type
- U is covariant and appears only as a method result type
  
So the method is checks out OK.

### Variance and List
Let’s get back to the previous implementation of lists. One shortcoming was that `Nil` had to be a class, whereas we would prefer it to be an object (after all, there is only one empty list). Can we change that? Yes, because we can make List covariant. Here are the essential modifications:

```scala
trait List[+T]
...
object Empty extends List[Nothing]
...
```

Here a definition of lists that implements all the cases we have seen so far:
```scala
trait List[+T]:
  def isEmpty = this match
    case Nil => true
    case _ => false
  override def toString =
    def recur(prefix: String, xs: List[T]): String = xs match
        case x :: xs1 => s"$prefix$x${recur(", ", xs1)}"
        case Nil => ")"
    recur("List(", this)

case class ::[+T](head: T, tail: List[T]) extends List[T]
case object Nil extends List[Nothing]
extension [T](x: T) def :: (xs: List[T]): List[T] = ::(x, xs)
object List:
  def apply() = Nil
  def apply[T](x: T) = x :: Nil
  def apply[T](x1: T, x2: T) = x1 :: x2 :: Nil
  ...
```

### Making Classes Covariant

Sometimes, we have to put in a bit of work to make a class covariant. Consider adding a prepend method to List which prepends a given element, yielding a new list. A first implementation of prepend could look like this:

```scala
trait List[+T]:
  def prepend(elem: T): List[T] = ::(elem, this)
```
But that does not work!


The above code not type-check?  Why

Possible answers:
- [ ] prepend turns List into a mutable class.
- [x] prepend fails variance checking.
- [ ] prepend’s right-hand side contains a type error

Indeed, the compiler is right to throw out List with prepend, because it violates the Liskov Substitution Principle: Here’s something one can do with a list xs of type List[Fruit]:

```scala
xs.prepend(Orange)
```
But the same operation on a list ys of type List[Apple] would lead to a type error:

```scala
ys.prepend(Orange)
```
So, `List[Apple]` cannot be a subtype of `List[Fruit]`.

But prepend is a natural method to have on immutable lists! How can we make it variance-correct?
We can use a lower bound:

```scala
def prepend [U >: T] (elem: U): List[U] = ::(elem, this)
```

This passes variance checks, because:
- covariant type parameters may appear in lower bounds of method type parameters
- contravariant type parameters may appear in upper bounds.


### Exercise 

Assume prepend in trait List is implemented like this:

```scala
def prepend [U >: T] (elem: U): List[U] = ::(elem, this)
```

What is the result type of this function:

```scala
def f(xs: List[Apple], x: Orange) = xs.prepend(x) ?
```

Possible answers:
- [ ] does not type check
- [ ] List[Apple]
- [ ] List[Orange]
- [x] List[Fruit]
- [ ] List[Any]



The need for a lower bound was essentially to decouple the new parameter of the class and the parameter of the newly created object. Using an extension method such as in `::` above, sidesteps the problem and is often simpler:

```scala
extension [T](x: T): 
  def :: (xs: List[T]): List[T] = ::(x, xs)
```