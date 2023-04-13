# List


## List Methods

Sub-lists and element access
|   Method  |   Description  |
| --- | --- |
`xs.length` |The number of elements of `xs`.
`xs.last` | The list’s last element, exception if `xs` is empty.
`xs.init` | A list consisting of all elements of `xs` except the last one, exception if `xs` is empty.
`xs.take(n)` |A list consisting of the first `n` elements of `xs`, or `xs` itself if it is shorter than `n`.
`xs.drop(n)` | The rest of the collection after taking `n` elements.
`xs(n)` | The element of `xs` at index `n`. Equivalent to `xs.apply(n)`.



Creating new lists:
|   Method  |   Description  |
| --- | --- |
`xs ++ ys` | The list consisting of all elements of `xs` followed by all elements of `ys`.
`xs.reverse`  | The list containing the elements of `xs` in reversed order.
`xs.updated(n, x)` | The list containing the same elements as `xs`, except at index `n` where it contains `x`.


Finding elements:

|   Method  |   Description  |
| --- | --- |
`xs.indexOf(x)` | The index of the first element in `xs` equal to `x`, or `-1` if `x` does not appear in `xs`.
`xs.contains(x)` | same as `xs.indexOf(x) >= 0`

## Implementations

### last

The complexity of head is (small) constant time. What is the complexity of last? To find out, let’s write a possible implementation of last as a stand-alone function.

```scala
def last[T](xs: List[T]): T = xs match
  case List() => throw Error("last of empty list")
  case List(x) => x
  case y :: ys => last(ys)
```

So, `last` takes steps proportional to the length of the list `xs`.


### Init

```scala
def init[T](xs: List[T]): List[T] = xs match
  case List() => throw Error("last of empty list")
  case List(x) => List()
  case y :: ys => y::init(ys)
```
The complexity of `init` is also the length of list `xs`

### Concatenation

How can concatenation be implemented? Let’s try by writing an extension method for `++`:

```scala
extension [T](xs: List[T])
  def ++ (ys: List[T]): List[T] = xs match
    case Nil => ys
    case x :: xs1 => x:: (xs1 ++ ys)
```
What is the complexity of `concat`? Answer: `O(xs.length)`


### Reverse

How can reverse be implemented? Let’s try by writing an extension method:

```scala
extension [T](xs: List[T])
  def reverse: List[T] = xs match
    case Nil => Nil
    case y :: ys => ys.reverse ++ List(y)
```
What is the complexity of `reverse`? Answer: `O(xs.length*xs.length)`

### Remove

Remove the `n`’th element of a list `xs`. If `n` is out of bounds,return `xs` itself.

```scala
def removeAt[T](n: Int, xs: List[T]):List[T] =  xs match
    case Nil => Nil
    case y::ys =>
        if n==0 then ys 
        else y::removeAt(n-1,ys)
```

Usage example:

```scala
removeAt(1, List('a', 'b', 'c', 'd')) //: List(a, c, d)
```

### Flatten

Flatten a list structure:
```scala
def flatten(xs: Any): List[Any] = xs match
    case Nil => Nil
    case y::ys => flatten(y)++flatten(ys)
    case _ => xs::Nil
```

Usage example:

```scala
flatten(List(List(1, 1), 2, List(3, List(5, 8)))) //: List[Any] = List(1, 1, 2, 3, 5, 8)
```

I

## Tuples and Generic Methods

### Sorting Lists Faster

As a non-trivial example, let’s design a function to sort lists that is more efficient than insertion sort. A good algorithm for this is merge sort. The idea is as follows:

If the list consists of zero or one elements, it is already sorted.
Otherwise, take the following steps:
- Separate the list into two sub-lists, each containing around half of the elements of the original list.
- Sort the two sub-lists.
- Merge the two sorted sub-lists into a single sorted list.

```scala
def merge(xs: List[Int], ys: List[Int]):List[Int] = (xs, ys) match
  case (Nil, ys) => ys
  case (xs, Nil) => xs
  case (x :: xs1, y :: ys1) =>
    if x < y then x :: merge(xs1, ys)
    else y :: merge(xs, ys1)

def msort(xs: List[Int]): List[Int] =
  val n = xs.length / 2
  if n == 0 then xs
  else
    val (fst, snd) = xs.splitAt(n)
    merge(msort(fst), msort(snd))
```

Usage example:

```scala
msort(List(2,3,1,7,6,4))
// res0: List[Int] = List(1, 2, 3, 4, 6, 7)
```

The splitAt function on lists returns two sublists
- the elements up the the given index
-  the elements from that index
The lists are returned in a pair.

```scala
val xs = List(2,3,1,7,6,4)
// xs: List[Int] = List(2, 3, 1, 7, 6, 4)

val n = xs.length / 2
// n: Int = 3

xs.splitAt(n)
// res1: Tuple2[List[Int], List[Int]] = (List(2, 3, 1),List(7, 6, 4))
```
### Pair and Tuples

The pair consisting of x and y is written (x, y) in Scala.

`val pair = ("answer", 42)` > `pair : (String, Int) = (answer,42)`

The type of pair above is (String, Int).

Pairs can also be used as patterns:

`val (label, value) = pair` > `label: String = answer, value: Int = 42`

This works analogously for tuples with more than two elements.

```scala
extension [T](xs: List[T])
  def splitAt(n:Int) = (xs.taken(n),xs.drop(n))
```
### The Tuple classes

For small n,  the tuple type `(T1, ..., Tn)` is an abbreviation of the parameterized type `scala.Tuplen[T1, ..., Tn]`.

A tuple expression `(e1, ..., en)` is equivalent to the function application `scala.Tuplen(e1, ..., en)`. 

A tuple pattern `(p1, ..., pn)` is equivalent to the constructor pattern `scala.Tuplen(p1, ..., pn)`. 

Currently, small n can be up to 22. There’s also a `TupleXXL` class that handles Tuples larger than that limit. 

Here, all `Tuplen` classes are modeled after the following pattern:

```scala
case class Tuple2[T1, T2](_1: +T1, _2: +T2):
  override def toString = "(" + _1 + "," + _2 +")"
```
The fields of a tuple can be accessed with names _1, _2, … So instead of the pattern binding
```scala
val (label, value) = pair
```
one could also have written:

```scala
val label = pair._1
val value = pair._2
```
But the pattern matching form is generally preferred.


### Making Sort More General

How to parameterize `msort` so that it can also be used for lists
with elements other than Int?

```scala
def msort[T](xs: List[T]): List[T] = ???
```
does not work, because the comparison < in merge is not defined for
arbitrary types T.

Idea: Parameterize merge with the necessary comparison function


The most flexible design is to make the function sort polymorphic and to
pass the comparison operation as an additional parameter:

```scala
def mergeP[T](xs: List[T], ys: List[T])(lt: (T, T) => Boolean):List[T] = (xs, ys) match
  case (Nil, ys) => ys
  case (xs, Nil) => xs
  case (x :: xs1, y :: ys1) =>
    if lt(x, y) then x :: mergeP(xs1, ys)(lt)
    else y :: mergeP(xs, ys1)(lt)

def msortP[T](xs: List[T])(lt: (T, T) => Boolean):List[T]  = 
  val n = xs.length / 2
  if n == 0 then xs
  else
    val (fst, snd) = xs.splitAt(n)
    mergeP(msortP(fst)(lt), msortP(snd)(lt))(lt)
```

Usage example:

```scala
val nums = List(-5, 6, 3, 2, 7)
// nums: List[Int] = List(-5, 6, 3, 2, 7)
val fruits = List("apple", "pear", "orange", "pineapple")
// fruits: List[String] = List(apple, pear, orange, pineapple)

msortP(nums)((x: Int, y: Int) => x < y)
// res2: List[Int] = List(-5, 2, 3, 6, 7)
msortP(fruits)((x: String, y: String) => x.compareTo(y) < 0)
// res3: List[String] = List(apple, orange, pear, pineapple)
```

## Higher Order List Function

The examples have shown that functions on lists often have similar
structures. We can identify several recurring patterns, like,

- transforming each element in a list in a certain way,
- retrieving a list of all elements satisfying a criterion,
- combining the elements of a list using an operator.
  
Functional languages allow programmers to write generic functions that
implement patterns such as these using higher-order functions.

### Mapping

A common operation is to transform each element of a list and then
return the list of results. For example, to multiply each element of a list by the same factor, you
could write:

```scala
def scaleList(xs: List[Double], factor: Double): List[Double] = xs match
  case Nil => xs
  case y :: ys => y * factor :: scaleList(ys, factor)
```

This scheme can be generalized to the method `map` of the `List` class. A simple way to define map is as follows:

```scala
extension [T](xs: List[T])
def map[U](f: T => U): List[U] = xs match
  case Nil => xs
  case x :: xs => f(x) :: xs.map(f)
```

In fact, the actual definition of map is a bit more complicated, because it is tail-recursive, and also because it works for arbitrary collections, not just lists.

Using `map`, `scaleList` can be written more concisely.

```scala
def scaleList(xs: List[Double], factor: Double) =
  xs.map(x => x * factor)
```

Consider a function to square each element of a list, and return the result. Complete the two following equivalent definitions of squareList.

```scala
def squareList(xs: List[Int]): List[Int] = xs match
  case Nil => Nil
  case y :: ys => y * y :: squareList(ys)

def squareList(xs: List[Int]): List[Int] =
  xs.map(x => x * x)
```

### Filtering

Another common operation on lists is the selection of all elements
satisfying a given condition. For example:

```scala
def posElems(xs: List[Int]): List[Int] = xs match
  case Nil => xs
  case y :: ys => if y > 0 then y :: posElems(ys) else posElems(ys) 
```

This pattern is generalized by the method filter of the List class:

```scala
extension [T](xs: List[T])
  def filter(p: T => Boolean): List[T] = this match
    case Nil => this
    case x :: xs => if p(x) then x :: xs.filter(p) else xs.filter(p)
```

Using filter, posElems can be written more concisely.

```scala
def posElems(xs: List[Int]): List[Int] =
xs.filter(x => x > 0)
```

Besides filter, there are also the following methods that extract sublists based on a predicate:

|   Method  |   Description  |
| --- | --- |
`xs.filterNot(p)` | Same as `xs.filter(x => !p(x))`; The list consisting of those elements of xs that do not satisfy the predicate p.
`xs.partition(p)` | Same as (`xs.filter(p)`, `xs.filterNot(p)`), but computed in a single traversal of the list xs.
`xs.takeWhile(p)` | The longest prefix of list xs consisting of elements that all satisfy the predicate p.
`xs.dropWhile(p)` | The remainder of the list xs after any leading elements satisfying p have been removed.
`xs.span(p)` | Same as `(xs.takeWhile(p)`,  xs.dropWhile(p))` but computed in a single traversal of the list xs.

#### Exercise I
Write a function pack the packs consecutive duplicates of list elements into sub-list. For instance, `pack(List("a","a","a","b","c","c","a"))`, should give `List(List("a","a","a"),List("b"),List("c","c"),List("a"))`.

```scala
def pack[T](xs: List[T]):List[List[T]] = xs match
  case Nil => Nil
  case x :: xs1 => 
    val (v1, v2) = xs.span(t=>t==x)
    v1::pack(v2)
```
#### Exercise II
Using pack, write a function encode that produces the run-length encoding of a list. The idea is to encode n consecutive duplicates of an element x as a `pair (x, n)`. For instance,
`encode(List("a", "a", "a", "b", "c", "c", "a"))` should give
`List(("a", 3), ("b", 1), ("c", 2), ("a", 1))`.

```scala
def encode[T](xs:List[T]):List[(T,Int)]=  
  pack(xs).map(x=>(x.head,x.length))
```

## Reduction of Lists

Another common operation on lists is to combine the elements of a list using a given operator. For example:

```scala
sum(List(x1, ..., xn)) = 0 + x1 + ... + xn
product(List(x1, ..., xn)) = 1 * x1 * ... * xn
```

We can implement this with the usual recursive schema:

```scala
def sum(xs: List[Int]): Int = xs match
  case Nil => 0
  case y :: ys => y + sum(ys)
```

### ReduceLeft

This pattern can be abstracted out using the generic method `reduceLeft`: `reduceLeft` inserts a given binary operator between adjacent elements of a list:

```scala
List(x1, ..., xn).reduceLeft(op) = x1.op(x2). ... .op(xn)
```
 Using `reduceLeft`, we can simplify:

```scala
def sum(xs: List[Int]) = (0 :: xs).reduceLeft((x, y) => x + y)
def product(xs: List[Int]) = (1 :: xs).reduceLeft((x, y) => x * y)
```

Instead of `((x, y) => x * y))`, one can also write shorter:
`(_ * _)`. Every `_` represents a new parameter, going from left to right. The parameters are defined at the next outer pair of parentheses (or the whole expression if there are no enclosing parentheses). So, `sum` and `product` can also be expressed like this:

```scala
def sum(xs: List[Int]) = (0 :: xs).reduceLeft(_ + _)
def product(xs: List[Int]) = (1 :: xs).reduceLeft(_ * _)
```

### FoldLeft

The function `reduceLeft` is defined in terms of a more general function, `foldLeft`. `foldLeft` is like `reduceLeft` but takes an accumulator, `z`, as an additional parameter, which is returned when `foldLeft` is called on an empty list. 

```scala
List(x1, ..., xn).foldLeft(z)(op) = z.op(x1).op ... .op(xn)
```
So, sum and product can also be defined as follows:

```scala
def sum(xs: List[Int]) = xs.foldLeft(0)(_ + _)
def product(xs: List[Int]) = xs.foldLeft(1)(_ * _)
```

Applications of `foldLeft` and `reduceLeft` unfold on trees that lean to the left. They have two dual functions, `foldRight` and `reduceRight`, which produce trees which lean to the right.

#### Implementations


```scala
abstract class List[T]:
  def reduceLeft(op: (T, T) => T): T = this match
    case Nil => throw IllegalOperationException(”Nil.reduceLeft”)
    case x :: xs => xs.foldLeft(x)(op)
  def foldLeft[U](z: U)(op: (U, T) => U): U = this match
    case Nil => z
    case x :: xs => xs.foldLeft(op(z, x))(op)
```

```scala
def reduceRight(op: (T, T) => T): T = this match
  case Nil => throw UnsupportedOperationException("Nil.reduceRight")
  case x :: Nil => x
  case x :: xs => op(x, xs.reduceRight(op))
def foldRight[U](z: U)(op: (T, U) => U): U = this match
  case Nil => z
  case x :: xs => op(x, xs.foldRight(z)(op))
```

For operators that are associative and commutative, `foldLeft` and `foldRight` are equivalent(even though there may be a difference in efficiency). Often, foldLeft, for instances, can be implemented in a terror recursive way, whereas foldRight is not recursive, so it needs more stack space. But sometimes, only one of the two operators is appropriate.

#### Exercise III

Here is another formulation of `concat`:
```scala
def concat[T](xs: List[T], ys: List[T]): List[T] =
  xs.foldRight(ys)(_ :: _)
```

Here, it isn’t possible to replace foldRight by foldLeft. Why?

- [x] The types would not work out
- [ ] The resulting function would not terminate
- [ ] The result would be reversed


### Reversing List

We now develop a function for reversing lists which has a linear cost. The idea is to use the operation `foldLeft`:

```scala
def reverse[a](xs: List[T]): List[T] =
  xs.foldLeft(List[T]())((xs, x) => x :: xs)  
```
The complexity of this implementation of `reverse` is linear in xs.

#### Exercise VI

Complete the following definitions of the basic functions map and length
on lists, such that their implementation uses foldRight:

```scala
def mapFun[T, U](xs: List[T], f: T => U): List[U] =
  xs.foldRight(List[U]())( (t, u) => f(t)::u )

def lengthFun[T](xs: List[T]): Int =
  xs.foldRight(0)((t, n) => n + 1)
```

## Reasoning about Lists

Recall the concatenation operation ++ on lists.We would like to verify that concatenation is associative, and that it admits the empty list Nil as neutral element to the left and to the right:

```
(xs ++ ys) ++ zs = xs ++ (ys ++ zs)
xs ++ Nil = xs
Nil ++ xs = xs
```

We can prove properties like these by **structural induction** on lists. 

### Natural Induction

The principle of proof by **natural induction** is as follows: to show a property `p(n)` for all the integers n ≥ b,
- Show that we have P(b) (base case),
- for all integers n ≥ b show the induction step: if one has P(n), then one also has P(n + 1).

#### Example 

Given:
```scala
def factorial(n: Int): Int =
  if n == 0 then 1 // 1st clause
  else n * factorial(n-1) // 2nd clause
```

Show that, for all n >= 4, factorial(n) >= power(2, n)

- Base Case: 4. 
  This case is established by simple calculations:
  ```
  factorial(4) = 24 >= 16 = power(2, 4)
  ```
- Induction step: n+1
  We have for n >= 4:
  ```
  factorial(n + 1)
  >= (n + 1) * factorial(n) // by 2nd clause in factorial
  > 2 * factorial(n) // by calculating
  >= 2 * power(2, n) // by induction hypothesis
  = power(2, n + 1) // by definition of power
  ```


Note that a proof can freely apply reduction steps as equalities to some part of a term. That works because pure functional programs don’t have side effects; so that a term is equivalent to the term to which it reduces. This principle is called **referential transparency**.


### Structural Induction

The principle of structural induction is analogous to natural induction: to prove a property `P(xs)` for all lists `xs`,
- show that `P(Nil)` holds (*base case*),
- for a list `xs` and some element `x`, show the induction step: if `P(xs)` holds, then `P(x :: xs)` also holds.


#### Example 

Let’s show that, for lists `xs`, `ys`, `zs`:

```
(xs ++ ys) ++ zs = xs ++ (ys ++ zs)
```
To do this, use **structural induction** on `xs`. From the previous implementation of `++`,

```scala
extension [T](xs: List[T]
  def ++ (ys: List[T]) = xs match
    case Nil => ys
    case x :: xs1 => x :: (xs1 ++ ys)
```

distill two defining clauses of ++:

```
Nil ++ ys = ys // 1st clause
(x :: xs1) ++ ys = x :: (xs1 ++ ys) // 2nd clause
```


- Base case: Nil
  For the left-hand side we have:

  ```
  (Nil ++ ys) ++ zs
  = ys ++ zs // by 1st clause of ++
  ```

  For the right-hand side, we have:
  ```
  Nil ++ (ys ++ zs)
  = ys ++ zs // by 1st clause of ++
  ```
  This case is therefore established

- Induction step: x :: xs
  For the left-hand side, we have:
  ```
  ((x :: xs) ++ ys) ++ zs
  = (x :: (xs ++ ys)) ++ zs // by 2nd clause of ++
  = x :: ((xs ++ ys) ++ zs) // by 2nd clause of ++
  = x :: (xs ++ (ys ++ zs)) // by induction hypothesis
  ```
  For the right hand side we have:

  ```
   (x :: xs) ++ (ys ++ zs)
   = x :: (xs ++ (ys ++ zs)) // by 2nd clause of ++
   ```
   So this case (and with it, the property) is established.

#### Exercise VI

Show by induction on `xs` that `xs ++ Nil = xs`. How many equations do you need for the inductive step?
- [x] 2
- [ ] 3
- [ ] 4


- Induction step: x :: xs
  
  For the left-hand side, we have:
  ```
  (x :: xs) ++ Nil
  = x :: (xs ++ Nil) // by 2nd clause of ++
  = x :: xs // by induction hypothesis
  ```

  For the right hand side we have:

  ```
  (x :: xs) 
  ```
  So this case (and with it, the property) is established.