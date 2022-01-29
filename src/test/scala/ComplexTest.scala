import org.scalactic.TripleEqualsSupport.Spread
import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._

/** Helper method for comparing doubles */
object DoubleHelper:
  val epsilon = 0.0000001

  implicit class FuzzyDouble(d: Double):
    def unary_~ : Spread[Double] =
      d +- epsilon

import DoubleHelper._

class ComplexTest extends AnyFlatSpec:

  "Complex number" should "support addition" in {
    val c1 = Complex(3.5, 0)
    val c2 = Complex(-1.5, 3)
    val cSum = c1 + c2
    cSum.re should equal(~2.0)
    cSum.im should equal(~3.0)
  }

  it should "support multiplication" in {
    val c1 = Complex(3.5, -1)
    val cMult = c1 * 2.0
    cMult.re should equal(~7.0)
    cMult.im should equal(~(-2.0))
  }

  it should "support squaring" in {
    val c = Complex(2, 3)
    val cSquared = c ** 2
    cSquared.re should equal(~(-5.0))
    cSquared.im should equal(~12.0)
  }

  it should "have a magnitude" in {
    val c = Complex(2, 2)
    val magnitudeSquared = 8.0
    c.magnitudeSquared should equal(~magnitudeSquared)
    c.magnitude should equal(~Math.sqrt(magnitudeSquared))
  }
