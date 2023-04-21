val capitalOfCountry = Map("US"->"Washington")
def showCapital(country: String) = capitalOfCountry.get(country) match
  case Some(capital) => capital
  case None => "missing data"

showCapital("US") 
showCapital("Andorra") 

// capitalOfCountry("US")
// capitalOfCountry("Andorra")

// capitalOfCountry.get("US")
// capitalOfCountry.get("Andorra")

val m1 = Map("red" -> 1, "blue" -> 2)
val m2 = m1 + ("blue" -> 3)
val m3 = m1 + ("blue" -> 2)
val m4 = m1 ++ m3

val fruit = List("apple", "pear", "orange", "pineapple")
fruit.sortWith(_.length < _.length) 
fruit.sorted

fruit.groupBy(_.head) 