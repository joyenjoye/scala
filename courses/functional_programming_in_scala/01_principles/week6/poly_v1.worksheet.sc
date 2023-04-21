class Polynom(nonZeroTerms: Map[Int, Double]):
  
  val terms = nonZeroTerms.withDefaultValue(0.0)

  def + (other: Polynom) =
    Polynom(terms ++ other.terms.map((exp, coeff) => (exp, terms(exp) + coeff)))

  override def toString =
    val termStrings =
    for (exp, coeff) <- terms.toList.sorted.reverse
    yield
      val exponent = if exp == 0 then "" else s"x^$exp"
      s"$coeff$exponent"
    if terms.isEmpty then "0" else termStrings.mkString(" + ")


val a = Polynom(Map(1 -> 2.0, 3 -> 4.0, 5 -> 6.0))
a.terms
a.toString

val b = Polynom(Map(1 -> 3.0, 2 -> 3.0, 4 -> 1.0))
b.terms
b.toString

a.terms++b.terms
(a+b).toString


a+b