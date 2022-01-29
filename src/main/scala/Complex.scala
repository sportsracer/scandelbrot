import scala.annotation.targetName

case class Complex(re: Double, im: Double):

  lazy val magnitudeSquared = re * re + im * im
  lazy val magnitude = Math.sqrt(magnitudeSquared)

  inline def +(other: Complex): Complex =
    Complex(re + other.re, im + other.im)

  inline def *(factor: Double): Complex =
    Complex(re * factor, im * factor)

  @targetName("power")
  inline def **(pow: 2): Complex =
    Complex(re * re - im * im, 2 * re * im)

  override def toString(): String =
    s"$re + ${im}i"
