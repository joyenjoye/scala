abstract class IntSet:
  def incl(x: Int): IntSet
  def contains(x: Int): Boolean

object IntSet:
  def apply(): IntSet = Empty()
  def apply(x: Int): IntSet = Empty().incl(x)
  def apply(x: Int, y: Int): IntSet = Empty().incl(x).incl(y)
  
class Empty() extends IntSet:
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = NonEmpty(x, Empty(), Empty())

class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet:
  def contains(x: Int): Boolean =
    if x < elem then left.contains(x)
    else if x > elem then right.contains(x)
    else true
  def incl(x: Int): IntSet =
    if x < elem then NonEmpty(elem, left.incl(x), right)
    else if x > elem then NonEmpty(elem, left, right.incl(x))
    else this
end NonEmpty


val a: Array[NonEmpty] = Array(NonEmpty(1, Empty(), Empty()))
val b: Array[IntSet] = a
b(0) = Empty()
val s: NonEmpty = a(0)