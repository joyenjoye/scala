# Class and Substitutions

## Extension Methods

Having to define all methods that belong to a class inside the class itself can lead to very large classes, and is not very modular. Methods that do not need to access the internals of a class can
alternatively be defined as extension methods. For instance, we can add min and abs methods to class Rational like this:

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
- Extensions cannot refer to other class members via this
 


# Operators

In principle, the rational numbers defined by Rational are as natural as integers. But for the user of these abstractions, there is a noticeable difference:
- We write x + y, if x and y are integers, but
- We write r.add(s) if r and s are rational numbers.
  
In Scala, we can eliminate this difference. We proceed in two steps.



# Precedence Rules

The precedence of an operator is determined by its first character.
The following table lists the characters in increasing order of priority
precedence:
|   | 
|:--------:|
all letters
\|
^
&
< >
= !
:
\+ -
\* / %
|   | 

(all other special characters)

