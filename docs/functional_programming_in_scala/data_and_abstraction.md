# Data and Abstraction

## Class Hierarchies

The below section are notes from slides of coursera course [Principle of Functional Programming](https://d3c33hcgiwev3.cloudfront.net/zukfJeuAR_epHyXrgMf3Fw_e287337d7f18475da2e56c172ffe4de1_progfun1-3-1.pdf?Expires=1672790400&Signature=B9Yh2NFjLSDi3KxqAVazq8VV1meJwb4eJHdzGwWxrxbZYbpSuv6jEb8MD9O5yFljhk07j0L2pkm56gX5G7f~L3Nij~qFiUP4SplkUJgEl3TV-egAXcG4au3XEB5X7ekDPnd2rhQsml7FPLmYxc1d2cmSwW3mBKdq1dfvK~xZeeo_&Key-Pair-Id=APKAJLTNE6QMUY6HBC5A)


### Abstract Classes

Consider the task of writing a class for sets of integers with the following operations.

```scala
abstract class IntSet:
def incl(x: Int): IntSet
def contains(x: Int): Boolean
```

IntSet is an abstract class. Abstract classes can contain members which are missing an implementation (in our case, both incl and contains); these are called abstract members. Consequently, no direct instances of an abstract class can be created, for instance an `IntSet()` call would be illegal.

### Class Extensions

```scala
class Empty() extends IntSet:
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = NonEmpty(x, Empty(), Empty())

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet:
  def contains(x: Int): Boolean =
    if x < elem then left.contains(x)
    else if x > elem then right.contains(x)
    else true
  def incl(x: Int): IntSet =
    if x < elem then NonEmpty(elem, left.incl(x), right)
    else if x > elem then NonEmpty(elem, left, right.incl(x))
    else this
end NonEmpty
```

### Base Classes and Subclasses

In the above example,  `IntSet` is called the superclass of `Empty` and `NonEmpty`. `Empty` and `NonEmpty` are subclasses of IntSet. In Scala, any user-defined class extends another class. If no superclass is given, the standard class Object in the Java package `java.lang` is assumed. The direct or indirect superclasses of a class C are called base classes of C. So, the base classes of NonEmpty include IntSet and Object


### Implementation and Overriding

The definitions of contains and incl in the classes `Empty` and `NonEmpty` implement the abstract functions in the base trait `IntSet`.It is also possible to redefine an existing, non-abstract definition in a subclass by using override.

```scala
abstract class Base: 
def foo = 1 
def bar: Int 
```

```scala
class Sub extends Base:
override def foo = 2
def bar = 3
```

### Object Definitions

In the IntSet example, one could argue that there is really only a single
empty IntSet.So it seems overkill to have the user create many instances of it.
We can express this case better with an object definition:

```scala
object Empty extends IntSet:
def contains(x: Int): Boolean = false
def incl(x: Int): IntSet = NonEmpty(x, Empty, Empty)
end Empty
```
This defines a singleton object named `Empty`. No other `Empty` instance can be (or needs to be) created.
Singleton objects are values, so `Empty` evaluates to itself.

### Companion Objects

An object and a class can have the same name. This is possible since Scala has two global namespaces: one for types and one for values. Classes live in the type namespace, whereas objects live in the term namespace.

If a class and object with the same name are given in the same sourcefile, we call them companions. For example:

```scala
class IntSet ...
object IntSet:
def singleton(x: Int) = NonEmpty(x, Empty, Empty)
```

This defines a method to build sets with one element, which can be called as IntSet. singleton(elem). A *companion object* of a class plays a role similar to static class definitions in Java (which are absent in Scala).


### Programs





### Dynamic Binding

Object-oriented languages (including Scala) implement dynamic method dispatch. This means that the code invoked by a method call depends on the runtime type of the object that contains the method.


## How classes are organized

### Package

### Imports

Imports come in several forms:

```scala
import week3.Rational // imports just Rational
import week3.{Rational, Hello} // imports both Rational and Hello
import week3._ // imports everything in package week3
```
The first two forms are called named imports. The last form is called a wildcard import. You can import from either a package or an object.

Some entities are automatically imported in any Scala program. These are:

- All members of package scala
- All members of package java.lang
- All members of the singleton object `scala.Predef`.
  
Here are the fully qualified names of some types and functions which you
have seen so far:
|   | | 
|:--------:|:--------:|
`Int` |`scala.Int`
`Boolean` |`scala.Boolean`
`Object` |`java.lang.Object`
`require` |`scala.Predef.require`
`assert` |`scala.Predef.assert`
|   | 

You can explore the standard Scala library using the `scaladoc` web pages.
You can start at www.scala-lang.org/api/current

### Traits

In Java, as well as in Scala, a class can only have one superclass. But what if a class has several natural supertypes to which it conforms or from which it wants to inherit code? Here, you could use traits.A trait is declared like an abstract class, just with trait instead of abstract class.

```scala
trait Planar:
 def height: Int
 def width: Int
 def surface = height * width
```

Classes, objects and traits can inherit from at most one class but arbitrary many traits. For example:

```scala
class Square extends Shape, Planar, Movable ...
```
Traits resemble interfaces in Java, but are more powerful because they can have parameters and can contain fields and concrete methods.

### Exceptions
Scala’s exception handling is similar to Java’s. The expression

```scala
throw Exc
```
aborts evaluation with the exception Exc. The type of this expression is Nothing

## Polymorphism

A fundamental data structure in many functional languages is the immutable linked list. It is constructed from two building blocks:
`Nil` the empty list
`Cons` a cell containing an element and the remainder of the list.


```scala
trait IntList ...
class Cons(val head: Int, val tail: IntList) extends IntList ...
class Nil() extends IntList ...
```

Note the abbreviation (val head: Int, val tail: IntList) in the definition of Cons. This defines at the same time parameters and fields of a class. It is equivalent to:

```scala
class Cons(_head: Int, _tail: IntList) extends IntList:
val head = _head
val tail = _tail
```
where _head and _tail are otherwise unused names.

### Type Parameters

It seems too narrow to define only lists with Int elements. We’d need another class hierarchy for Double lists, and so on, one for each possible element type. We can generalize the definition using a type parameter:

```scala
trait List[T]
class Cons[T](val head: T, val tail: List[T]) extends List[T]
class Nil[T] extends List[T]
```

Type parameters are written in square brackets, e.g. [T]

```scala
trait List[T]:
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
class Cons[T](val head: T, val tail: List[T]) extends List[T]:
  def isEmpty = false
class Nil[T] extends List[T]:
  def isEmpty = true
  def head = throw new NoSuchElementException("Nil.head")
  def tail = throw new NoSuchElementException("Nil.tail")
```


Like classes, functions can have type parameters. For instance, here is a function that creates a list consisting of a single element.

```scala
def singleton[T](elem: T) = Cons[T](elem, Nil[T])

```

We can then write:

```scala
singleton[Int](1)
singleton[Boolean](true)
```

In fact, the Scala compiler can usually deduce the correct type parameters from the value arguments of a function call. So, in most cases, type parameters can be left out. You could also write:

```scala
singleton(1)
singleton(true)
```
### Types and Evaluation
Type parameters do not affect evaluation in Scala. We can assume that all type parameters and type arguments are removed before evaluating the program. This is also called type erasure. Languages that use type erasure include Java, Scala, Haskell, ML, OCaml. Some other languages keep the type parameters around at run time, these include C++, C#, F#.

Polymorphism means that a function type comes “in many forms”. In programming it means that
- the function can be applied to arguments of many types, or
- the type can have instances of many types.

We have seen two principal forms of polymorphism:
- subtyping: instances of a subclass can be passed to a base class
- generics: instances of a function or class are created by type
parameterization.

### Exercise

Write a function nth that takes a list and an integer n and selects the n’th
element of the list.

```scala
def nth[T](xs: List[T], n: Int): Int = ???
```

Elements are numbered from 0. If index is outside the range from 0 up the the length of the list minus one,
a IndexOutOfBoundsException should be thrown.

## Objects 

A pure object-oriented language is one in which every value is an object. If the language is based on classes, this means that the type of each value is a class. Is Scala a pure object-oriented language? At first glance, there seem to be some exceptions: primitive types, functions. But, let’s look closer.

Conceptually, types such as Int or Boolean do not receive special treatment in Scala. They are like the other classes, defined in the package scala. For reasons of efficiency, the Scala compiler represents the values of type scala.Int by 32-bit integers, and the values of type scala.Boolean by Java’s Booleans, etc.

The Boolean type maps to the JVM’s primitive type boolean. But one could define it as a class from first principles:

```scala
package idealized.scala
abstract class Boolean extends AnyVal:
  def ifThenElse[T](t: => T, e: => T): T
  def && (x: => Boolean): Boolean = ifThenElse(x, false)
  def || (x: => Boolean): Boolean = ifThenElse(true, x)
  def unary_!: Boolean = ifThenElse(false, true)
  def == (x: Boolean): Boolean = ifThenElse(x, x.unary_!)
  def != (x: Boolean): Boolean = ifThenElse(x.unary_!, x)
  ...
end Boolean
```


Here are constants true and false that go with Boolean in
idealized.scala:

```scala
package idealized.scala
object true extends Boolean:
  def ifThenElse[T](t: => T, e: => T) = t 
object false extends Boolean:
  def ifThenElse[T](t: => T, e: => T) = e
```

### Exercise I

Provide an implementation of an implication operator `==>` for class `idealized.scala.Boolean`.

```scala
extension (x: Boolean):
  def ==> (y: Boolean) = x.ifThenElse(y, true)
```
That is, if x is true, y has to be true also, whereas if x is false, y can be arbitrary


Here is a partial specification of the class `scala.Int`.

```scala
class Int:
  def + (that: Double): Double
  def + (that: Float): Float
  def + (that: Long): Long
  def + (that: Int): Int // same for -, *, /, %
  def << (cnt: Int): Int // same for >>, >>> */
  def & (that: Long): Long
  def & (that: Int): Int // same for |, ^ */
  def == (that: Double): Boolean
  def == (that: Float): Boolean
  def == (that: Long): Boolean // same for !=, <, >, <=, >=
  ...
end Int
```

Can it be represented as a class from first principles (i.e. not using primitive ints)?


### Exercise II

Provide an implementation of the abstract class Nat that represents non-negative integers.

```scala
abstract class Nat:
  def isZero: Boolean
  def predecessor: Nat
  def successor: Nat
  def + (that: Nat): Nat
  def - (that: Nat): Nat
end Nat
```

Do not use standard numerical classes in this implementation. Rather, implement a sub-object and a sub-class:

```scala
object Zero extends Nat:
...
class Succ(n: Nat) extends Nat:
...
```

One for the number zero, the other for strictly positive numbers.

### Functions as Objects

In fact function values are treated as objects in Scala. The function type A => B is just an abbreviation for the class
scala.Function1[A, B], which is defined as follows.

```scala
package scala
trait Function1[A, B]:
def apply(x: A): B
```

So functions are objects with apply methods. There are also traits Function2, Function3, … for functions which take more parameters.

An anonymous function such as
```scala
(x: Int) => x * x
```
is expanded to:

```scala
new Function1[Int, Int]:
def apply(x: Int) = x * x
```

This anonymous class can itself be thought of as a block that defines and
instantiates a local class:

```scala
{ class $anonfun() extends Function1[Int, Int]:
def apply(x: Int) = x * x
$anonfun()
}
```