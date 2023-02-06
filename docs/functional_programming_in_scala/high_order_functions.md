# Higher Order Functions


- [Higher-Order Functions](#higher-order-functions-1)
  - [Anonymous Function](#anonymous-function)
  - [Function returning Function](#function-returning-function)
  - [Consecutive Stepwise Applications](#consecutive-stepwise-applications)
  - [Multiple Parameter Lists](#multiple-parameter-lists)
- [Example: Finding Fixed Points](#example-finding-fixed-points)
- [Function and Data](#function-and-data)
  - [Classes](#classes)
  - [Method](#method)
- [Data Abstraction](#data-abstraction)
  - [Self Reference](#self-reference)
  - [Preconditions](#preconditions)
  - [Assertions](#assertions)
  - [Constructors](#constructors)
  - [End Markers](#end-markers)
- [Evaluation and Operators](#evaluation-and-operators)
  - [Extension Methods](#extension-methods)
- [Operators](#operators)
- [Precedence Rules](#precedence-rules)

## Higher-Order Functions

Take the sum of the integers between a and b:

```scala
def sumInts(a: Int, b: Int): Int =
    if a > b then 0 else a + sumInts(a + 1, b)
```

Take the sum of the cubes of all the integers between a and b :

```scala
def cube(x: Int): Int = x * x * x
def sumCubes(a: Int, b: Int): Int =
if a > b then 0 else cube(a) + sumCubes(a + 1, b)
```

Take the sum of the factorials of all the integers between a and b :

```scala
def fact(x:Int):Int = if x==0 then 1 else x * fact(x-1)
def sumFactorials(a: Int, b: Int): Int =
if a > b then 0 else factorial(a) + sumFactorials(a + 1, b)
```

Functional languages treat functions as first-class values. This means that, like any other value, a function can be passed as a parameter and returned as a result. This provides a flexible way to compose programs. Functions that take other functions as parameters or that return functions as results are called higher order functions.

If we pass function as parameter, Let’s define:

```scala
def sum(f: Int => Int, a: Int, b: Int): Int =
  if a > b then 0
  else f(a) + sum(f, a + 1, b)
```

We can then write:

```scala
def sumInts(a: Int, b: Int) = sum(id, a, b)
def sumCubes(a: Int, b: Int) = sum(cube, a, b)
def sumFactorials(a: Int, b: Int) = sum(fact, a, b)
```

where

```scala
def id(x: Int): Int = x
def cube(x: Int): Int = x * x * x
def fact(x: Int): Int = if x == 0 then 1 else x * fact(x - 1)
```

The type A => B is the type of a function that takes an argument of type A and returns a result of type B.

### Anonymous Function

A function that raises its argument to a cube:

```scala
(x: Int) => x * x * x
```

Here, `(x: Int)` is the parameter of the function, and `x * x * x` is it’s body.

### Function returning Function

Let’s rewrite sum as follows.

```scala
def sum(f: Int => Int): (Int, Int) => Int =
  def sumF(a: Int, b: Int): Int =
  if a > b then 0
  else f(a) + sumF(a + 1, b)
  sumF
```

`sum` is now a function that returns another function. The returned function `sumF` applies the given function parameter `f` and sums the results.

We can then define:

```scala
def sumInts = sum(x => x)
def sumCubes = sum(x => x * x * x)
def sumFactorials = sum(fact)
```

### Consecutive Stepwise Applications

We cam avoid the middlemen such as `sumInts`, `sumCubes`,

```scala
sum (cube) (1, 10)
```

sum(cube) applies sum to cube and returns the sum of cubes function.

- `sum(cube)` is therefore equivalent to sumCubes.
- `This function` is next applied to the arguments `(1, 10)`.

### Multiple Parameter Lists

The definition of functions that return functions is so useful in functional programming that there is a special syntax for it in Scala. For example, the following definition of sum is equivalent to the one with the nested sumF function, but shorter:

```scala
def sum(f: Int => Int)(a: Int, b: Int): Int =
  if a > b then 0 else f(a) + sum(f)(a + 1, b)
```

## Example: Finding Fixed Points

A number x is called a fixed point of a function f if

$$f(x) = x$$

For some functions f we can locate the fixed points by starting with an initial estimate and then by applying f in a repetitive way.

$$x, f(x), f(f(x)), f(f(f(x))), ...$$

until the value does not vary anymore (or the change is sufficiently small). This leads to the following function for finding a fixed point:

```scala
val tolerance = 0.0001
def isCloseEnough(x: Double, y: Double) =
  abs((x - y) / x) < tolerance
def fixedPoint(f: Double => Double)(firstGuess: Double): Double =
  def iterate(guess: Double): Double =
    val next = f(guess)
    if isCloseEnough(guess, next) then next
    else iterate(next)
  iterate(firstGuess)

```

## Function and Data

In this section, we’ll learn how functions create and encapsulate data structures.

✨ **Example: Rational Numbers**

We want to design a package for doing rational arithmetic. A rational number $x/y$ is represented by two integers: its numerator $x$, and its denominator $y$.

Suppose we want to implement the addition of two rational numbers.

```scala
def addRationalNumerator(n1: Int, d1: Int, n2: Int, d2: Int): Int
def addRationalDenominator(n1: Int, d1: Int, n2: Int, d2: Int): Int
```

but it would be difficult to manage all these numerators and denominators. A better choice is to combine the numerator and denominator of a rational number in a data structure.

### Classes

In Scala, we do this by defining a `class`:

```scala
class Rational(x: Int, y: Int):
  def numer = x
  def denom = y
```

This definition introduces two entities:

- A new type, named Rational.
- A constructor Rational to create elements of this type.

Scala keeps the names of types and values in different namespaces. So there’s no conflict between the two entities named Rational. We call the elements of a class type objects. We create an object by calling the constructor of the class.

**✨Example**

```scala
val x = Rational(1, 2)
x.numer
x.denom
```

### Method

One can go further and also package functions operating on a data abstraction in the data abstraction itself. Such functions are called methods.

**✨Example**
Rational numbers now would have, in addition to the functions numer and denom, the functions `add`, `sub`, `mul`,`toString`.

```scala
class Rational(x: Int, y: Int):
  def numer = x
  def denom = y
  def add(r:Rational)=
    Rational(numer *r.denom+r.numer*denom, denom*r.denom)

  def mul(r:Rational)=
    Rational(numer *r.numer, denom*r.denom)

  def neg = Rational(-numer,denom)

  def sub(r:Rational) = add(r.neg)

  override def toString(): String = s"$numer/$denom"

end Rational
```

**Remark**: the modifier `override` declares that `toString` redefines a method
that already exists (in the class `java.lang.Object`).

Here is how one might use the new `Rational` abstraction:

```scala
val x = Rational(1, 3)
val y = Rational(5, 7)
val z = Rational(3, 2)
x.add(y).mul(z)
```

## Data Abstraction

The previous example has shown that rational numbers aren’t always represented in their simplest form. (Why?)

One would expect the rational numbers to be simplified: reduce them to their smallest numerator and denominator by dividing both with a divisor.

We could implement this in each rational operation, but it would be easy to forget this division in an operation.

A better alternative consists of simplifying the representation in the class when the objects are constructed:

```scala
class Rational(x: Int, y: Int):
  private def gcd(a: Int, b: Int): Int =
    if b == 0 then a else gcd(b, a % b)

  private val g = gcd(x, y)
  def numer = x / g
  def denom = y / g
```

`gcd` and `g` are private members; we can only access them from inside the `Rational` class. In this example, we calculate `gcd` immediately, so that its value can be
re-used in the calculations of `numer` and `denom`.

It is equally possible to turn `numer` and `denom` into vals, so that they are computed only once:

```scala
class Rational(x: Int, y: Int):
  private def gcd(a: Int, b: Int): Int =
    if b == 0 then a else gcd(b, a % b)
  val numer = x / gcd(x, y)
  val denom = y / gcd(x, y)
```

This can be advantageous if the functions `numer` and `denom` are called often.

Clients observe exactly the same behavior in each case. This ability to choose different implementations of the data without affecting clients is called **data abstraction**. It is a cornerstone of software engineering.

### Self Reference

On the inside of a class, the name `this` represents the object on which the current method is executed.

**✨Example**

Add the functions `less` and `max` to the class Rational.

```scala
class Rational(x: Int, y: Int):
  def less(that: Rational): Boolean =
    numer * that.denom < that.numer * denom
  def max(that: Rational): Rational =
    if this.less(that) then that else this
```

### Preconditions

Let’s say our `Rational` class requires that the denominator is positive. We can enforce this by calling the `require` function.

```scala
class Rational(x: Int, y: Int):
  require(y > 0, "denominator must be positive")
```

`require` is a predefined function. It takes a condition and an optional message string. If the condition passed to require is `false`, an `IllegalArgumentException` is thrown with the given message string.

### Assertions

Besides `require`, there is also `assert`. Assert also takes a condition and an optional message string as parameters. E.g.

```scala
val x = sqrt(y)
assert(x >= 0)
```

Like `require`, a failing assert will also throw an exception, but it’s a
different one: `AssertionError` for assert, `IllegalArgumentException` for
require.

This reflects a difference in intent

- `require` is used to enforce a precondition on the caller of a function.
- `assert` is used as to check the code of the function itself.

### Constructors

In Scala, a class implicitly introduces a constructor. This one is called the
primary constructor of the class. The primary constructor takes the parameters of the class and executes all statements in the class body.

Scala also allows the declaration of auxiliary constructors. These are methods named `this`

**✨Example**

Adding an auxiliary constructor to the class `Rational`.

```scala
class Rational(x: Int, y: Int):
  def this(x: Int) = this(x, 1)

Rational(2) > 2/1
```

### End Markers

With longer lists of definitions and deep nesting, it’s sometimes hard to see where a class or other construct ends. End markers are a tool to make this explicit. End marker is followed by the name that’s defined in the
definition that ends at this point. It must align with the opening keyword

```scala
class Rational(x: Int, y: Int):
  def this(x: Int) = this(x, 1)
  ...
end Rational
```

<!-- ## Class and Substitutions -->

## Evaluation and Operators

### Extension Methods

Having to define all methods that belong to a class inside the class itself can lead to very large classes, and is not very modular. Methods that do not need to access the internals of a class can alternatively be defined as extension methods. For instance, we can add `min` and `abs` methods to class `Rational` like this:

```scala
extension (r: Rational)
  def min(s: Rational): Rational = if s.less(r) then s else r
  def abs: Rational = Rational(r.numer.abs, r.denom)
```

Extensions of a class are visible if they are listed in the companion object of a class (as in the code above) or if they defined or imported in the current scope.

Members of a visible extensions of class C can be called as if they were members of C. E.g.

```scala
Rational(1/2).min(Rational(2/3))
```

Caveats:

- Extensions can only add new members, not override existing ones.
- Extensions cannot refer to other class members via `this`.

# Operators

In principle, the rational numbers defined by `Rational` are as natural as integers. But for the user of these abstractions, there is a noticeable difference:

- We write `x+ y`, if `x` and `y` are integers, but
- We write `r.add(s)` if `r` and `s` are rational numbers.

In Scala, we can eliminate this difference. We proceed in two steps.

- Step 1: Relaxed Identifiers. Since operators are identifiers, it is possible to use them as method names

      ```scala
      extension (x: Rational)
      def + (y: Rational): Rational = x.add(y)
      def * (y: Rational): Rational = x.mul(y)
      ...
      This allows rational numbers to be used like Int or Double:
      val x = Rational(1, 2)
      val y = Rational(1, 3)
      x * x + y * y
      ```

  Step 2: Infix Notation. An operator method with a single parameter can be used as an infix operator. An alphanumeric method with a single parameter can also be used as an infix operator if it is declared with an infix modifier. E.g.

```scala
extension (x: Rational)
  infix def min(that: Rational): Rational = ...
```

It is therefore possible to write

|         |     |          |
| ------- | --- | -------- |
| r + s   |     | r.+(s)   |
| r < s   |     | r.<(s)   |
| r min s |     | r.min(s) |
|         |     |          |

# Precedence Rules

The precedence of an operator is determined by its first character.
The following table lists the characters in increasing order of priority
precedence:
| |
|:--------:|
all letters
\|
^
&
< >
= !
:
\+ - \* / %
all other special characters
| |


