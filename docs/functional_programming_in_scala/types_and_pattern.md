# Types and Pattern

- [Types and Pattern](#types-and-pattern)
  - [Decomposition](#decomposition)
  - [List](#list)
  - [Enumeration](#enumeration)

## Pattern Matching

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

def eval(e: Expr): Int =
  if e.isNumber then e.numValue
  else if e.isSum then eval(e.leftOp) + eval(e.rightOp)
  else throw Error("Unknown expression" + e)

```

To integrate Prod and Var into Hierarchy, how many new method do we need?



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
- the empty list Nil, and
- the construction operation :: (pronounced cons):
  
x :: xs gives a new list with the first element x, followed by the elements of xs.

Convention: Operators ending in “:” associate to the right. A :: B :: C is interpreted as A :: (B :: C). We can thus omit the parentheses in the definition above.

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
|     |     |
| --- | --- |
  `Nil` | The `Nil` constant
`p :: ps` | A pattern that matches a list with a head matching `p` and a tail matching `ps`.
`List(p1, ..., pn)` | same as `p1 :: ... :: pn :: Nil`

### Exercise I

Consider the pattern `x :: y :: List(xs, ys) :: zs`. What is the condition that describes most accurately the length L of the
lists it matches?

- [ ] L == 3
- [ ] L == 4
- [ ] L == 5
- [ ] L >= 3
- [X] L >= 4
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
