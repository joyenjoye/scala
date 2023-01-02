// The sum func uses linear recurion. Write a tial-recursive version 

def sum(f: Int => Int, a: Int, b: Int): Int = 
    def loop(a: Int, acc:Int): Int = 
        if a>b then acc
        else loop(a+1,acc+f(a))
    loop(a,0)


def sumInts(a: Int, b: Int) = sum(x => x, a, b)

def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)


sumInts(1,3)


sumCubes(1,3)