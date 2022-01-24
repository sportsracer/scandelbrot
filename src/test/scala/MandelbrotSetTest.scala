import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._

class MandelbrotSetTest extends AnyFlatSpec {

  "MandelbrotSet" should "contain these numbers" in {
    assert(MandelbrotSet contains Complex(0, 0))
    assert(MandelbrotSet contains Complex(0.25, 0))
    assert(MandelbrotSet contains Complex(0, 0.5))
  }

  it should "not contain these numbers" in {
    assert(!(MandelbrotSet contains Complex(1, 0)))
    assert(!(MandelbrotSet contains Complex(0, 2)))
    assert(!(MandelbrotSet contains Complex(-4, -4)))
  }

  it should "compute fewer steps for 0.3 than for 1.3" in {
    val steps1 = MandelbrotSet.iterate(Complex(0.3, 0))
    val steps2 = MandelbrotSet.iterate(Complex(1.3, 0))
    assert(steps1.isDefined)
    assert(steps2.isDefined)
    steps1.get should be > steps2.get
  }

}
