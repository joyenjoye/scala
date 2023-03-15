# List


## List Methods

Sublists and element access
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

`val pair = ("answer", 42)` > pair : (String, Int) = (answer,42)

The type of pair above is (String, Int).

Pairs can also be used as patterns:

`val (label, value) = pair` > label: String = answer, value: Int = 42

This works analogously for tuples with more than two elements


For small  `n`, the tuple type `(T1, ..., Tn)` is an abbreviation of the parameterized type `scala.Tuplen[T1, ..., Tn]`. A tuple expression `(e1, ..., en)` is equivalent to the function application `scala.Tuplen(e1, ..., en)`. A tuple pattern `(p1, ..., pn)` is equivalent to the constructor pattern `scala.Tuplen(p1, ..., pn)`. Currently, "small" = up to 22. There’s also a `TupleXXL` class that handles Tuples larger than that limit.

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