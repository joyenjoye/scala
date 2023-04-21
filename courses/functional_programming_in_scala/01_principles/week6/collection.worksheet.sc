val nums = Vector(1, 2, 3, -88)
val people = Vector("Bob", "James", "Peter")


val xs: Array[Int] = Array(1, 2, 3)
xs.map(x => 2 * x)


val ys: String = "Hello world!"
ys.filter(_.isUpper)

val r: Range = 1 until 5
val s: Range = 1 to 5
1 to 10 by 3
6 to 1 by -2
1 until 9

// (1 until 7)
// .flatMap(i => (1 until i).map(j => (i, j)))
// .filter((x, y) => isPrime(x + y))

def scalarProduct(xs: List[Double], ys: List[Double]) : Double =
    (for 
       (x, y) <- xs.zip(ys)
    yield x * y).sum

val xs1=List(2,3,4.0)
scalarProduct(xs1,xs1)  

def otherProduct(xs: List[Double], ys: List[Double]) : Double =
    (for 
       x<-xs;
       y<-ys
    yield x * y).sum
    
otherProduct(xs1,xs1)  


val data = List(1, 1, 2, 3, 5, 8)

def loopA(values: List[Int]) =
  for x <- values yield x * x

def loopB(values: List[Int]) =
  (0 to (values.size)).map { x =>
    x * x
  }

def loopC(values: List[Int]) =
  values.map(x => x * x)

loopA(data)

loopB(data)

loopC(data)


def mystery(xs: Seq[Int]) =
  val ys =
    for x <- xs
    if x % 2 == 0
    yield x * x
  ys.sum

mystery(data)


def mystery2(xs: Seq[Int]) =
  xs
    .filter(x => x % 2 == 0)
    .map(x => x * x)
    .sum
mystery2(data)

def mystery3(xs: Seq[Int]) =
  for x <- xs
  if x % 2 == 0
  yield xs.sum
mystery3(data)

def checks(col: Int, delta: Int, queens: List[Int]): Boolean = queens match
  case qcol :: others =>
    qcol == col // vertical check
    || (qcol - col).abs == delta // diagonal check
    || checks(col, delta + 1, others)
  case Nil =>
    false

def isSafe(col: Int, queens: List[Int]): Boolean =
  !checks(col, 1, queens)

def queens(n: Int) =
  def placeQueens(k: Int): Set[List[Int]] =
    if k == 0 then Set(List())
    else
      for
        queens <- placeQueens(k - 1)
        col <- 0 until n
        if isSafe(col, queens)
      yield col :: queens
  placeQueens(n)

queens(4)