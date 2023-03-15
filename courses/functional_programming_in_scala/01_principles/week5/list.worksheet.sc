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