def sum(f: Int => Int): (a: Int,b: Int)=>Int = 
    if a>b then 0
    else f(a) + sumF(a+1,b)

sum(x=>x*x*x)(1,10)

// The idea that you can write a function as as sequence of annomymous functions that take aa single parameters
//  is called currying. 
// It is named as Haskell