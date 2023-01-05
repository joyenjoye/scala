trait List[T]:
  def isEmpty: Boolean
  def head: T
  def tail: List[T]

class Cons[T](val head: T, val tail: List[T]) extends List[T]:
  def isEmpty = false
  
class NIL[T] extends List[T]:
  def isEmpty = true
  def head = throw new NoSuchElementException("Nil.head")
  def tail = throw new NoSuchElementException("Nil.tail")

def nth[T](xs:List[T], n:Int):T = 
  if xs.isEmpty then throw IndexOutOfBoundsException()
  else if n==0 then xs.head
  else nth(xs.tail,n-1)

// nth(Cons(1,Cons(2,Cons(3,NIL()))),3)


nth(Cons(1,Cons(2,Cons(3,NIL()))),2)