package recfun

object RecFun extends RecFunInterface:

  def main(args: Array[String]): Unit =
    println("Pascal's Triangle")
    for row <- 0 to 10 do
      for col <- 0 to row do
        print(s"${pascal(col, row)} ")
      println()

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = 
    if c>r then throw IllegalArgumentException(s"c must be less than or equal to r")
    if (r < 0) | (r<0) then throw IllegalArgumentException(s"r and c must be non negative")
    if  (c == 0) | (c==r) then 1
    else pascal(c,r-1) + pascal(c-1,r-1)

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = 

    def find_bracket(x: List[Char], status:Boolean): Boolean = 
      println(x.mkString)
      if x.isEmpty then {if status then true else false}
      else {
      if status then {
        if x.head.toString ==")" then false else {
        if x.head.toString == "(" then find_bracket(x.tail.reverse, !status)
        else find_bracket(x.tail,status)
        }
        }
      else {
        if x.head.toString == ")" then find_bracket(x.tail.reverse,!status)
        else find_bracket(x.tail,status)
      }
    }
    val status = true
    find_bracket(chars,status)
   


  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = ???
