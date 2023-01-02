// The ability to choose different implementations of the data without 
// affecting the clients is called <<data abstraction>>

class Rational(x:Int, y:Int):

  //require 
  require(y>0, s"denominator must be positive, was $x/$y")

  //on the inside of a class, the name this represents the object on which current method is executed
  def this(x:Int) = this(x,1)
  private def gcd(a:Int, b:Int):Int = 
    if b==0 then a else gcd(b,a%b)


  def numer = x/gcd(x.abs,y)
  def denom = y/gcd(x.abs,y)

  def add(r:Rational)=
    Rational(numer *r.denom+r.numer*denom, denom*r.denom)

  def mul(r:Rational)=
    Rational(numer *r.numer, denom*r.denom)

  def neg = Rational(-numer,denom)

  def sub(r:Rational) = add(r.neg)

  def less(that:Rational): Boolean = 
    this.numer*that.denom < that.numer * this.denom
  
  def max(that:Rational):Rational = 
    if this.less(that) then that else this
  
  override def toString(): String = s"$numer/$denom"

//<<End Markers>>
//With longer lists of definitions and deep nesting, 
//it's sometimes hard to see where a class or other construct ends. 
//End Markers followed by the name that defined in th definition.
end Rational


val x = Rational(1,3)
val y = Rational(5,7)
val z = Rational(3,2)

x.add(y).mul(z)


y.neg
x.add(y.neg)
x.sub(y).sub(z)