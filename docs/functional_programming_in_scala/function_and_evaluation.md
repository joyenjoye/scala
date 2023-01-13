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

- In a restricted sense, a functional programming language is one which does not have mutable variables, assignments, or imperative control structures.
- In a wider sense, a functional programming language enables the construction of elegant programs that focus on functions and immutable data structures.
- In particular, functions in a FP language are first-class citizens. This
means 
  - they can be defined anywhere, including inside other functions 
  - like any other value, they can be passed as parameters to functions and returned as results
  - as for other values, there exists a set of operators to compose functions

Some functional programing languages are
  - Lisp, Scheme, Racket, Clojure
  - SML, Ocaml, F#
  - Haskell
  - Scala

