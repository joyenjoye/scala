
abstract class CodeTree
case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree
case class Leaf(char: Char, weight: Int) extends CodeTree


def weight(tree: CodeTree): Int = tree match {
case Fork(left, right, chars, weight) => weight
case Leaf(char, weight) => weight
}

def chars(tree: CodeTree): List[Char] = tree match {
case Fork(left, right, chars, weight) => chars
case Leaf(char, weight) => List(char)
}

def makeCodeTree(left: CodeTree, right: CodeTree) =
  Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))

def string2Chars(str: String): List[Char] = str.toList

def times(chars: List[Char]): List[(Char, Int)] = {
  def timesCalc(srtList: List[Char],l :List[(Char, Int)], ch : Char, count : Int ): List[(Char, Int)] = {
    if (srtList.isEmpty) l ::: (ch, count) :: Nil
    else if (ch != srtList.head) timesCalc(srtList.tail, l ::: (ch, count) :: Nil, srtList.head, 1)
    else timesCalc(srtList.tail, l , srtList.head, count+1)
  }

    val srtList = chars.sortBy(c => c)
    timesCalc(srtList.tail,Nil,srtList.head,1)
  }


def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = 
  def helper(freqs: List[(Char, Int)]): List[Leaf] = 
    if freqs.isEmpty then List()
    else Leaf(freqs.head._1, freqs.head._2)::helper(freqs.tail)

  helper(freqs.sortBy(_._2))


def singleton(trees: List[CodeTree]): Boolean = 
    if trees.length == 1 then true
    else false

def combine(trees: List[CodeTree]): List[CodeTree] = 
  if singleton(trees) | trees.isEmpty then trees
  else (makeCodeTree(trees.head,trees.tail.head)::trees.tail.tail).sortWith(weight(_) < weight(_))

def until(done: List[CodeTree] => Boolean, merge: List[CodeTree] => List[CodeTree])(trees: List[CodeTree]): List[CodeTree] =
  if done(trees) then trees
  else merge(trees)


def createCodeTree(chars: List[Char]): CodeTree = 
  if chars.isEmpty then throw java.util.NoSuchElementException("Empty String")
  else until(singleton, combine)(makeOrderedLeafList(times(chars))).head
  



type Bit = Int


def decode(tree: CodeTree, bits: List[Bit]): List[Char] = {
  def charDecode(t: CodeTree, bits: List[Bit]): List[Char] = t match {
    case Leaf(c, w) => if bits.isEmpty then List(c) else c :: charDecode(tree, bits)
    case Fork(l, r, c, w) => if bits.head == 0 then charDecode(l, bits.tail) else charDecode(r, bits.tail)
  }
  charDecode(tree,bits)
}

  
val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s',121895),Fork(Leaf('d',56269),Fork(Fork(Fork(Leaf('x',5928),Leaf('j',8351),List('x','j'),14279),Leaf('f',16351),List('x','j','f'),30630),Fork(Fork(Fork(Fork(Leaf('z',2093),Fork(Leaf('k',745),Leaf('w',1747),List('k','w'),2492),List('z','k','w'),4585),Leaf('y',4725),List('z','k','w','y'),9310),Leaf('h',11298),List('z','k','w','y','h'),20608),Leaf('q',20889),List('z','k','w','y','h','q'),41497),List('x','j','f','z','k','w','y','h','q'),72127),List('d','x','j','f','z','k','w','y','h','q'),128396),List('s','d','x','j','f','z','k','w','y','h','q'),250291),Fork(Fork(Leaf('o',82762),Leaf('l',83668),List('o','l'),166430),Fork(Fork(Leaf('m',45521),Leaf('p',46335),List('m','p'),91856),Leaf('u',96785),List('m','p','u'),188641),List('o','l','m','p','u'),355071),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u'),605362),Fork(Fork(Fork(Leaf('r',100500),Fork(Leaf('c',50003),Fork(Leaf('v',24975),Fork(Leaf('g',13288),Leaf('b',13822),List('g','b'),27110),List('v','g','b'),52085),List('c','v','g','b'),102088),List('r','c','v','g','b'),202588),Fork(Leaf('n',108812),Leaf('t',111103),List('n','t'),219915),List('r','c','v','g','b','n','t'),422503),Fork(Leaf('e',225947),Fork(Leaf('i',115465),Leaf('a',117110),List('i','a'),232575),List('e','i','a'),458522),List('r','c','v','g','b','n','t','e','i','a'),881025),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u','r','c','v','g','b','n','t','e','i','a'),1486387)


val secret: List[Bit] = List(0,0,1,1,1,0,1,0,1,1,1,0,0,1,1,0,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,1,0,1,0,1,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1)


def decodedSecret: List[Char] = decode(frenchCode,secret)

def encode(tree: CodeTree)(text: List[Char]): List[Bit] = 
  def charEncode(tr: CodeTree)(ch: Char):List[Bit] = tr match 
    case Leaf(c,w) => List()
    case Fork(l,r,c,w) => 
      if chars(l).contains(ch) then 0 :: charEncode(l)(ch) 
      else 1 :: charEncode(r)(ch)
  

  def _encode(text: List[Char]):List[Bit]= text match 
    case Nil => Nil
    case x :: xs => charEncode(tree)(x) ::: _encode(xs)
    

  _encode(text)

type CodeTable = List[(Char, List[Bit])]

def codeBits(table: CodeTable)(char: Char): List[Bit] = 
  table.filter(_._1 == char).head._2


def convert(tree: CodeTree): CodeTable = tree match
  case Leaf(c,w) => List((c, List()))
  case Fork(l,r,c,w) => mergeCodeTables(convert(l), convert(r))


def mergeCodeTables(left_table: CodeTable, right_table: CodeTable): CodeTable =
  def addBit(b: Bit)(elem: (Char, List[Bit])): (Char, List[Bit]) = (elem._1, b :: elem._2)
  left_table.map(addBit(0)) ::: right_table.map(addBit(1))

def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] = 
  if text.isEmpty then List()
  else codeBits(convert(tree))(text.head):::quickEncode(tree)(text.tail)





val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
val t2 = Fork(Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5), Leaf('d',4), List('a','b','d'), 9)
val decoded_val = decode(t1, encode(t1)("ab".toList))
encode(t1)("ab".toList)
convert(t1)
convert(t1.left)
t1.left
quickEncode(t1)("ab".toList)