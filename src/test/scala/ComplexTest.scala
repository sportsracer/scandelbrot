import org.scalactic.TripleEqualsSupport.Spread
import org.scalatest._
import org.scalatest.matchers.should.Matchers._


/** Helper method for comparing doubles */
object DoubleHelper {
  val epsilon = 0.0000001

  implicit class FuzzyDouble(d: Double) {
    def unary_~(): Spread[Double] = {
      d +- epsilon
    }
  }

}
import DoubleHelper._


class ComplexTest extends FlatSpec {

  "Complex number" should "support addition" in {
    val c1 = new Complex(3.5, 0)
    val c2 = new Complex(-1.5, 3)
    val cSum = c1 + c2
    cSum.re should equal (~2.0)
    cSum.im should equal (~3.0)
  }

  it should "support squaring" in {
    val c = new Complex(2, 3)
    val cSquared = c ** 2
    cSquared.re should equal (~(-5.0))
    cSquared.im should equal (~12.0)
  }

  it should "not support raising to a power other than 2" in {
    val c = new Complex(1, 0)
    an[IllegalArgumentException] should be thrownBy c ** 1
    an[IllegalArgumentException] should be thrownBy c ** 3
  }
  
  it should "have a magnitude" in {
    val c = new Complex(2, 2)
    c.magnitude() should equal (~Math.sqrt(8.0))
  }

}
