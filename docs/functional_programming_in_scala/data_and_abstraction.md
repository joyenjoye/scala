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

IntSet is an abstract class. Abstract classes can contain members which are missing an implementation (in our case, both incl and contains); these are called abstract members. Consequently, no direct instances of an abstract class can be created, for instance an IntSet() call would be illegal.

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

`IntSet` is called the superclass of Empty and NonEmpty.

`Empty` and `NonEmpty` are subclasses of IntSet.

In Scala, any user-defined class extends another class.

If no superclass is given, the standard class Object in the Java package `java.lang` is assumed.

The direct or indirect superclasses of a class C are called base classes of C. So, the base classes of NonEmpty include IntSet and Object


### Implementation and Overriding

The definitions of contains and incl in the classes `Empty` and `NonEmpty` implement the abstract functions in the base trait `IntSet`.It is also possible to redefine an existing, non-abstract definition in a subclass by using override.

```scala
abstract class Base: 
def foo = 1 
def bar: Int 

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

If a class and object with the same name are given in the same sourcefile,we call them companions. For example:

```scala
class IntSet ...
object IntSet:
def singleton(x: Int) = NonEmpty(x, Empty, Empty)
```

This defines a method to build sets with one element, which can be called as IntSet.singleton(elem). A *companion object* of a class plays a role similar to static class definitions in Java (which are absent in Scala).


### Dynamic Binding

Object-oriented languages (including Scala) implement dynamic method dispatch. This means that the code invoked by a method call depends on the runtime type of the object that contains the method.


## Polymorphism

Polymorphism means that a function type comes “in many forms”. In programming it means that
- the function can be applied to arguments of many types, or
- the type can have instances of many types.


## Objects