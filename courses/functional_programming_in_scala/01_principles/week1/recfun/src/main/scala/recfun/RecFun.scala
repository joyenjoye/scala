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

    def remove_bracket(x: List[Char], reversed:Boolean, remove_type:Boolean): List[Char] =
      println("*******")
      println(x.mkString)  
      if x.isEmpty then x
      else{
      if !reversed then {
        if x.head.toString ==")" then x
        else{
        if x.head.toString == "(" then {
          if remove_type then remove_bracket(x.tail.reverse, !reversed,remove_type)
        else remove_bracket(x.tail,!reversed,remove_type)
      }
        else remove_bracket(x.tail,reversed,remove_type)
        }
        }
      else {
        if x.head.toString == ")" then  {
          if remove_type then x.tail.reverse else x.tail}
        else remove_bracket(x.tail,reversed,remove_type)
      }
     }
  
    println("------")
    println(chars.mkString)
    if chars.isEmpty then true
    else {
          if chars.head.toString ==")" then false 
          else 
              {
                val reversed = false
                balance(remove_bracket(chars,reversed,true)) | balance(remove_bracket(chars,reversed,false)) 
              }
        }


  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = ???
