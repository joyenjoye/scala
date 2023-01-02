
// Using annymous functions, we can write sums in a shorter way.

def sum(f: Int => Int, a: Int, b: Int): Int = 
    if a> b then 0
    else f(a) +sum(f,a+1,b)


def sumInts(a: Int, b: Int) = sum(x => x, a, b)

def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)


sumInts(1,3)


sumCubes(1,3)