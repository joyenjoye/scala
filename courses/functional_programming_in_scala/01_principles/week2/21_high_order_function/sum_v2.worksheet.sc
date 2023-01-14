// type A=>B is the type of a function that takes an argument of type A and return a result of type B
def sum(f: Int => Int, a: Int, b: Int): Int = 
    if a> b then 0
    else f(a) +sum(f,a+1,b)

def id(x:Int): Int = x

def sumInts(a: Int, b: Int) = sum(id,a,b)


sumInts(1,3)

def cube(x:Int):Int = x*x*x

def sumCubes(a:Int, b:Int) = sum(cube,a,b)

sumCubes(1,3)

def fact(x:Int):Int = if x==0 then 1 else x* fact(x-1)

def sumFactorials(a:Int, b:Int) = sum(fact,a,b)

sumFactorials(1,3)