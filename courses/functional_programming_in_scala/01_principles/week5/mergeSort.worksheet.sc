def merge(xs: List[Int], ys: List[Int]):List[Int] = (xs, ys) match
  case (Nil, ys) => ys
  case (xs, Nil) => xs
  case (x :: xs1, y :: ys1) =>
    if x < y then x :: merge(xs1, ys)
    else y :: merge(xs, ys1)

def msort(xs: List[Int]): List[Int] =
  val n = xs.length / 2
  if n == 0 then xs
  else
    val (fst, snd) = xs.splitAt(n)
    merge(msort(fst), msort(snd))

def mergeP[T](xs: List[T], ys: List[T])(lt: (T, T) => Boolean):List[T] = (xs, ys) match
  case (Nil, ys) => ys
  case (xs, Nil) => xs
  case (x :: xs1, y :: ys1) =>
    if lt(x, y) then x :: mergeP(xs1, ys)(lt)
    else y :: mergeP(xs, ys1)(lt)

def msortP[T](xs: List[T])(lt: (T, T) => Boolean):List[T]  = 
  val n = xs.length / 2
  if n == 0 then xs
  else
    val (fst, snd) = xs.splitAt(n)
    mergeP(msortP(fst)(lt), msortP(snd)(lt))(lt)


msort(List(2,3,1,7,6,4))

val xs = List(2,3,1,7,6,4)
val n = xs.length / 2
xs.splitAt(n)

val nums = List(-5, 6, 3, 2, 7)
val fruits = List("apple", "pear", "orange", "pineapple")

msortP(nums)((x: Int, y: Int) => x < y)
msortP(fruits)((x: String, y: String) => x.compareTo(y) < 0)