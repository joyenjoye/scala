class Polynom(nonZeroTerms: Map[Int, Double]):
  def this(bindings: (Int, Double)*) = this(bindings.toMap)
  
  def terms = nonZeroTerms.withDefaultValue(0.0)

  def + (other: Polynom) =
    Polynom(other.terms.foldLeft(terms)(addTerm))

  def addTerm(terms: Map[Int, Double], term: (Int, Double)) =
    val (exp, coeff) = term
    terms + (exp, coeff + terms(exp))

  override def toString =
    val termStrings =
    for (exp, coeff) <- terms.toList.sorted.reverse
    yield
      val exponent = if exp == 0 then "" else s"x^$exp"
      s"$coeff$exponent"
    if terms.isEmpty then "0" else termStrings.mkString(" + ")


val a = Polynom(1 -> 2.0, 3 -> 4.0, 5 -> 6.0)
a.terms
a.toString

val b = Polynom(1 -> 3.0, 2 -> 3.0, 4 -> 1.0)
b.terms
b.toString

a.terms++b.terms
(a+b).toString


a+b