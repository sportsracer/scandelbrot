case class Complex(re: Double, im: Double) {

  def +(other: Complex): Complex = {
    Complex(re + other.re, im + other.im)
  }

  def **(pow: Int): Complex = {
    pow match {
      case 2 => Complex(re * re - im * im, 2 * re * im)
      case _ => throw new IllegalArgumentException("Only implemented for power 2")
    }
  }

  def magnitude(): Double = {
    Math.sqrt(re * re + im * im)
  }

  override def toString: String = {
    s"$re + ${im}i"
  }
}
