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

    def check_bracket(x: List[Char], bracket:List[Char]): List[Char] =

      if x.isEmpty then bracket

      else if x.head.toString =="(" then check_bracket(x.tail, x.head::bracket)
      
      else if x.head.toString == ")" then {

        if bracket.isEmpty then x.head::bracket

        else check_bracket(x.tail, bracket.tail)
      
        }
          
      else check_bracket(x.tail,bracket)
        
        
      
    if check_bracket(chars,"".toList).isEmpty then true
    else false


  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = 

    def get_change(amt: Int, coins:List[Int],change:List[Int],count:Int): (List[Int],Int) = {   
      if coins.isEmpty then (change,count)
      else {
        if amt-coins.head==0 then get_change(amt,coins.tail, change,count+1)
        else if amt-coins.head<0 then get_change(amt,coins.tail, change,count)
        else get_change(amt,coins.tail, amt-coins.head::change,count)
      }}
    
    def count_change(change: List[Int], coins: List[Int]): Int = 
       if change.length ==1 then countChange(change.head,coins)
       else countChange(change.head,coins)+count_change(change.tail,coins)
    
    if money <=0 then 0
    else {
      if coins.isEmpty then 0
      else {
        val _change_ = get_change(money,coins,List(),0)
        val count = _change_.tail.head
        val change = _change_.head
        if change.isEmpty then count
        else count +count_change(change,coins)
      
    }}
  