val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
val res1 = numbers.foldLeft(0)((m, n) => m + n)

val res2 = numbers.foldLeft(0)((m, n) => m + n)
val res3 = numbers.foldRight(0)((m, n) => m + n)

type Bit = Int
type CodeTable = List[(Char, List[Bit])]

def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable =
  def addOrMerge(acc: CodeTable, elem: (Char, List[Bit])): CodeTable = 
    acc.find(_._1 == elem._1) match {
    case Some((_, bits)) => acc
    case None => acc ::: List(elem)
    }

  (a ::: b).foldLeft(List[(Char, List[Bit])]())(addOrMerge)

val a = List(('A',List(0,1)))

val b = List(('B',List(0,2)),('A',List(0,1)))

val c = mergeCodeTables(a,b)

abstract class CodeTree
case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
case class Leaf(char: Char, weight: Int) extends CodeTree

def codeBits(table: CodeTable)(char: Char): List[Bit] = 
    table.filter(_._1 == char).head._2

def convert(tree: CodeTree): CodeTable = tree match
      case Leaf(c,w) => List( (c, List()) )
      case Fork(l,r,c,w) => mergeCodeTables(convert(l), convert(r))

def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = 
  if text.isEmpty then List()
  else codeBits(convert(tree))(text.head):::quickEncode(tree)(text.tail)

