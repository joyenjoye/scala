
trait FunSetsInterface:
  type FunSet = Int => Boolean

  def contains(s: FunSet, elem: Int): Boolean
  def singletonSet(elem: Int): FunSet
  def union(s: FunSet, t: FunSet): FunSet
  def intersect(s: FunSet, t: Int => Boolean): FunSet
  def diff(s: FunSet, t: FunSet): FunSet
  def filter(s: FunSet, p: Int => Boolean): FunSet
  def forall(s: FunSet, p: Int => Boolean): Boolean
  def exists(s: FunSet, p: Int => Boolean): Boolean
  def map(s: FunSet, f: Int => Int): FunSet
  def toString(s: FunSet): String


// **
//  * 2. Purely Functional Sets.
//  */
trait FunSets extends FunSetsInterface:
  /**
   * We represent a set by its characteristic function, i.e.
   * its `contains` predicate.
   */
  override type FunSet = Int => Boolean

  /**
   * Indicates whether a set contains a given element.
   */
  def contains(s: FunSet, elem: Int): Boolean = s(elem)

  /**
   * Returns the set of the one given element.
   */
  def singletonSet(elem: Int): FunSet =  (x: Int) => x == elem  


  /**
   * Returns the union of the two given sets,
   * the sets of all elements that are in either `s` or `t`.
   */
  def union(s: FunSet, t: FunSet): FunSet = (x: Int) => s(x)||t(x) 

  /**
   * Returns the intersection of the two given sets,
   * the set of all elements that are both in `s` and `t`.
   */
  def intersect(s: FunSet, t: FunSet): FunSet =  (x: Int) => s(x)&&t(x) 

  /**
   * Returns the difference of the two given sets,
   * the set of all elements of `s` that are not in `t`.
   */
  def diff(s: FunSet, t: FunSet): FunSet =  (x: Int) => s(x) && !t(x) 

  /**
   * Returns the subset of `s` for which `p` holds.
   */
  def filter(s: FunSet, p: Int => Boolean): FunSet = (x: Int) => s(x) && p(x) 


  /**
   * The bounds for `forall` and `exists` are +/- 1000.
   */
  val bound = 1000

  /**
   * Returns whether all bounded integers within `s` satisfy `p`.
   */
  def forall(s: FunSet, p: Int => Boolean): Boolean =
    def iter(a: Int): Boolean =
      if a>bound then true
      else if s(a) then
        p(a) && iter(a+1)
      else
        iter(a+1)
    iter(-bound)

  /**
   * Returns whether there exists a bounded integer within `s`
   * that satisfies `p`.
   */
  def exists(s: FunSet, p: Int => Boolean): Boolean = !forall(s,(x:Int) => !p(x))

  /**
   * Returns a set transformed by applying `f` to each element of `s`.
   */
  def map(s: FunSet, f: Int => Int): FunSet = (x:Int) => exists(s,(y:Int) => f(y)==x)

  /**
   * Displays the contents of a set
   */
  def toString(s: FunSet): String =
    val xs = for i <- (-bound to bound) if contains(s, i) yield i
    xs.mkString("{", ",", "}")

  /**
   * Prints the contents of a set on the console.
   */
  def printSet(s: FunSet): Unit =
    println(toString(s))

object FunSets extends FunSets
  
val x = FunSets.singletonSet(1)

val y = FunSets.singletonSet(2)

FunSets.contains(x,1)

val z = FunSets.union(x,y)

FunSets.contains(z,1)
FunSets.contains(z,2)

val o = FunSets.intersect(x,y)
FunSets.contains(o,1)

val q = FunSets.intersect(z,y)
FunSets.contains(q,2)

  
val s1 = FunSets.singletonSet(1)
val s2 = FunSets.union(s1,FunSets.singletonSet(3))
val s3 = FunSets.union(s2,FunSets.singletonSet(4))
val s4 = FunSets.union(s3,FunSets.singletonSet(5))
val s5 =  FunSets.union(s4,FunSets.singletonSet(7))
val s6 =  FunSets.union(s5,FunSets.singletonSet(1000))



val s7 = FunSets.map(s6,(x: Int) => x-1)
FunSets.contains(s7,0)
FunSets.contains(s7,1)
FunSets.contains(s7,2)

