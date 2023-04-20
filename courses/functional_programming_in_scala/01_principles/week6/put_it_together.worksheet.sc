class Coder(words: List[String]):
  val mnemonics = Map(
    '2' -> "ABC", '3' -> "DEF", '4' -> "GHI", '5' -> "JKL",
    '6' -> "MNO", '7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")

  /** Maps a letter to the digit it represents */
  val charCode: Map[Char, Char] =
    for
      (digit, str) <- mnemonics
      ltr <- str
    yield ltr -> digit

  /** Maps a word to the digit string it can represent */
  def wordCode(word: String): String =
    word.toUpperCase.map(charCode)

  /** Maps a digit string to all words in the dictionary that represent it */
  val wordsForNum: Map[String, List[String]] = 
    words.groupBy(wordCode).withDefaultValue(Nil)

  /** All ways to encode a number as a list of words */
  def encode(number: String): Set[List[String]] = 
    if number.isEmpty then Set(Nil)
    else
        for
            splitPoint <- (1 to number.length).toSet
            word <- wordsForNum(number.take(splitPoint))
            rest <- encode(number.drop(splitPoint))
        yield word :: rest



val word = "Scala"
val words = List(word,"is","fun")
val test = Coder(words)
test.charCode
test.wordCode(word)
test.wordsForNum


word.toUpperCase
word.toUpperCase.map(test.charCode)


val coder = Coder(List("Scala", "Python", "Ruby", "C", "rocks", "socks", "sucks", "works", "pack"))
val number = "7225276257"
(1 to number.length).toSet

for splitPoint <- (1 to number.length).toSet
yield splitPoint 

for splitPoint <- (1 to number.length).toSet
yield number.take(splitPoint)

val x = for splitPoint <- (1 to number.length).toSet
      word <- coder.wordsForNum(number.take(splitPoint))
      rest <- coder.encode(number.drop(splitPoint))
  yield word::rest



coder.encode(number).map(_.mkString(" "))
x.map(_.mkString(" "))

