def sumInts(a:Int, b: Int): Int = 
    if a>b then 0 else a + sumInts(a+1,b)

sumInts(1,3)


def cube(x:Int): Int = x*x*x

cube(2)


def sumCube(a: Int, b: Int): Int = 
    if a> b then 0 else cube(a) +sumCube(a+1,b)


sumCube(1,2)

def fact(x:Int):Int = if x==0 then 1 else x * fact(x-1)

def sumFactorials(a: Int, b:Int):Int = 
    if a >b then 0 else fact(a) + sumFactorials(a+1,b)


sumFactorials(1,2)