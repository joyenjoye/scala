# Function and Evaluation

## Imperative programming 

Imperative programming is about
- modifying mutable variables,
- using assignments
- and control structures such as if-then-else, loops, break, continue, return.

The most common informal way to understand imperative programs is as instruction sequences for a Von Neumann computer.

There’s a strong correspondence between
- Mutable variables ≈ memory cells
- Variable dereferences ≈ load instructions
- Variable assignments ≈ store instructions
- Control structures ≈ jumps

In the end, pure imperative programming is limited by the “Von Neumann” bottleneck:

>One tends to conceptualize data structures word-by-word.

We need other techniques for defining high-level abstractions such as collections, polynomials, geometric shapes, strings, documents. 

Ideally, develop a theory consists of
- one or more data types
- operations on these types
- laws that describe the relationships between values and operations

Normally, a theory does not describe mutations! For instance the theory of polynomials defines the sum of two polynomials by laws such as:

$$(a*x + b) + (c*x + d) = (a + c)*x + (b + d)$$

But it does not define an operator to change a coefficient while keeping the polynomial the same!
Whereas in an imperative program one can write:

```java
class Polynomial { double [] coefficient ; }
Polynomial p = ...;
p.coefficient [0] = 42;
```

If we want to implement high-level concepts following their mathematical theories, there’s no place for mutation.
- The theories do not admit it.
- Mutation can destroy useful laws in the theories.

Therefore, let’s
- concentrate on defining theories for operators expressed as functions,
- avoid mutations,
- have powerful ways to abstract and compose functions.

## Functional Programming

In a restricted sense, a functional programming language is one which does not have mutable variables, assignments, or imperative control structures. In a wider sense, a functional programming language enables the construction of elegant programs that focus on functions and immutable data structures. In particular, functions in a functional programming language are first-class citizens. This means 
  - they can be defined anywhere, including inside other functions 
  - like any other value, they can be passed as parameters to functions and returned as results
  - as for other values, there exists a set of operators to compose functions

Some functional programing languages are
  - Lisp, Scheme, Racket, Clojure
  - SML, Ocaml, F#
  - Haskell
  - Scala

Why Functional Programming Now?  It’s an effective tool to handle concurrency and parallelism, on every scale.Our computers are not Van-Neuman machines anymore. They have
  - parallel cores
  - clusters of servers
  - distribution in the cloud
This causes new programming challenges such as cache coherency and non-determinism.

## Functions and Parameters

Function parameters come with their type, which is given after a colon

```scala
def power(x: Double, y: Int): Double = ...
```

Primitive types are as in Java, but are written capitalized:
|     |     |     |
| --- | --- | --- |
Int | 32-bit |integers
Long |64-bit |integers
Float |32-bit |floating point numbers
Double |64-bit |floating point numbers
Char |16-bit |unicode characters
Short |16-bit |integers
Byte | 8-bit |integers
Boolean | boolean values| true and false

## Evaluation

Evaluation of non-primitive Expression is as follows.
1. Take the leftmost operator
2. Evaluate its operands (left before right)
3. Apply the operator to the operands

```scala
(2 * pi) * radius
(2 * 3.14159) * radius
6.28318 * radius
6.28318 * 10
62.8318
```

Applications of parameterized functions are evaluated in a similar way as operators:
1. Evaluate all function arguments, from left to right
2. Replace the function application by the function’s right-hand side, and, at the same time
3. Replace the formal parameters of the function by the actual arguments.

```scala
def square(x: Double) = x * 
def sumOfSquares(x: Double, y: Double) = square(x) + square(y)
```
Given `square` and `sumOfSquares`, `sumOfSquares(3, 2+2)`,

*call-by-value* is evaluated as follows

```scala
sumOfSquares(3, 2+2)
sumOfSquares(3, 4)
square(3) + square(4)
3 * 3 + square(4)
9 + square(4)
9 + 4 * 4
9 + 16
25
```

An alternative evaluation strategy- *call-by-name* is evaluated as follows
```scala
sumOfSquares(3, 2+2)
square(3) + square(2+2)
3 * 3 + square(2+2)
9 + square(2+2)
9 + (2+2) * (2+2)
9 + 4 * (2+2)
9 + 4 * 4
25
```

### Call-by-name v.s. call-by-value

Both strategies reduce to the same final values as long as
- he reduced expression consists of pure functions, and
- both evaluations terminate.
  
*Call-by-value* has the advantage that it evaluates every function argument only once. *Call-by-name* has the advantage that a function argument is not evaluated if the corresponding parameter is unused in the evaluation of the function body.

When termination is not guaranteed, if CBV evaluation of an expression e terminates, then CBN evaluation of e terminates, too. The other direction is not true.

Example of an expression that terminates under CBN but not under CBV is as follows:

```scala
def first(x: Int, y: Int) = x

first(1, loop).
```

### Scala’s evaluation strategy

Scala normally uses call-by-value. But if the type of a function parameter starts with => it uses *call-by-name*.

```scala
def constOne(x: Int, y: => Int) = 1
```

Let’s trace the evaluations of `constOne(1+2, loop)` and `constOne(loop, 1+2)`

```scala
constOne(1 + 2, loop)
constOne(3, loop)
1
```

```scala
constOne(loop, 1 + 2)
constOne(loop, 1 + 2)
constOne(loop, 1 + 2)
...
```

## Conditionals and Value Definitions

### Conditional Expressions

To express choosing between two alternatives, Scala has a conditional expression if-then-else. It resembles an if-else in Java, but is used for expressions, not statements. For example:

```scala
def abs(x: Int) = if x >= 0 then x else -x
```
x >= 0 is a predicate, of type Boolean.


### Value Definitions
We have seen that function parameters can be passed by value or be passed by name. The same distinction applies to definitions. The `def` form is “by-name”, its right hand side is evaluated on each use. There is also a `val` form, which is “by-value”. The difference between val and def becomes apparent when the right hand side does not terminate. For example:

```scala
val x = 2
val y = square(x)
```

The right-hand side of a `val` definition is evaluated at the point of the definition itself.Afterwards, the name refers to the value. For instance, `y` above refers to `4`, not `square(2)`.