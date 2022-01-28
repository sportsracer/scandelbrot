case class Complex(re: Double, im: Double):

  lazy val magnitudeSquared = re * re + im * im
  lazy val magnitude = Math.sqrt(magnitudeSquared)

  inline def +(other: Complex): Complex =
    Complex(re + other.re, im + other.im)

  inline def *(factor: Double): Complex =
    Complex(re * factor, im * factor)

  inline def **(pow: Int): Complex =
    pow match
      case 2 => Complex(re * re - im * im, 2 * re * im)
      case _ => throw IllegalArgumentException("Only implemented for power 2")

  override def toString(): String =
    s"$re + ${im}i"
