def last[T](xs: List[T]): T = xs match
  case List() => throw Error("last of empty list")
  case List(x) => x
  case y :: ys => last(ys)

def init[T](xs: List[T]): List[T] = xs match
  case List() => throw Error("last of empty list")
  case List(x) => List()
  case y :: ys => y::init(ys)

extension [T](xs: List[T])
  def ++ (ys: List[T]): List[T] = xs match
    case Nil => ys
    case x :: xs1 => x:: (xs1 ++ ys)

extension [T](xs: List[T])
  def reverse: List[T] = xs match
    case Nil => Nil
    case y :: ys => ys.reverse ++ List(y)

def removeAt[T](n: Int, xs: List[T]):List[T] =  xs match
    case Nil => Nil
    case y::ys =>
        if n==0 then ys 
        else y::removeAt(n-1,ys)

def flatten(xs: Any): List[Any] = xs match
    case Nil => Nil
    case y::ys => flatten(y)++flatten(ys)
     case _ => xs::Nil
    


removeAt(1, List('a', 'b', 'c', 'd'))
flatten(List(List(1, 1), 2, List(3, List(5, 8))))


val nums = List(1,2,3,4,5,6)

nums.partition(x=>x%2!=0)
nums.span(x=>x%2!=0)


def pack[T](xs: List[T]):List[List[T]] = xs match
  case Nil => Nil
  case x :: xs1 => 
    val (v1, v2) = xs.span(t=>t==x)
    v1::pack(v2)


pack(List("a","a","a","b","c","c","a"))



def encode[T](xs:List[T]):List[(T,Int)]=  
  pack(xs).map(x=>(x.head,x.length))
  
encode(List("a","a","a","b","c","c","a"))

val num_list = List(1,2,3,4,5,6,7)
num_list.foldLeft(0)(_ + _)
num_list.foldLeft(1)(_ * _)
num_list.foldRight(0)(_ + _)
val b = 2

val a = 1
List(a,b)
a::b::Nil
num_list::a::Nil


def new_reverse[T](xs: List[T]): List[T] =
  xs.foldLeft(List[T]())((xs, x) => x :: xs)  

new_reverse(num_list)
new_reverse(Nil)

def mapFun[T, U](xs: List[T], f: T => U): List[U] =
  xs.foldRight(List[U]())( (t, u) => f(t)::u )

mapFun(xs = num_list,f= x=>x+1)


def lengthFun[T](xs: List[T]): Int =
  xs.foldRight(0)((t, n) => n + 1)

lengthFun(xs = num_list)