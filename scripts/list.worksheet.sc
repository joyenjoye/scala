
// Creating Lists
val ints = List(1, 2, 3)
val names = List("Joel", "Chris", "Ed")

// another way to construct a List
val namesAgain = "Joel" :: "Chris" :: "Ed" :: Nil


// You can also declare the List’s type, if you prefer, though it generally isn’t necessary:
// val ints: List[Int] = List(1, 2, 3)
// val names: List[String] = List("Joel", "Chris", "Ed")

// One exception is when you have mixed types in a collection; in that case you may want to explicitly specify its type:
val things: List[String | Int | Double] = List(1, "two", 3.0) 
val thingsAny: List[Any] = List(1, "two", 3.0)              

val a = List(1, 2, 3)
val b = 0 :: a              
val c = List(-1, 0) ::: a  

0 +: a
a :+4


// How to loop over lists
for name <- names do println(name)