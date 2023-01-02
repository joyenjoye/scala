// in 21_high_order_function, we saw common pattern a,b at the end
// we try to eliminate them
// sum is a function that returns another function

def sum(f: Int => Int): (Int,Int)=>Int = 
  def sumF(a:Int, b: Int): Int =
    if a>b then 0
    else f(a) + sumF(a+1,b)
  sumF


def sumInts = sum(x=>x)
def sumCubes = sum(x=>x*x*x)

def fact(x:Int):Int = if x==0 then 1 else x* fact(x-1)

def sumFactorials = sum(fact)


sumCubes(1,10)+sumFactorials(10,20)

sum(x=>x*x*x)(1,10)


// The definition of functions that return functions is so useful in functional programming that
// there is a special syntax for it in scala.
