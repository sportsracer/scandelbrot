import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._

class MandelbrotSetTest extends AnyFlatSpec:

  "MandelbrotSet" should "contain these numbers" in {
    assert(Complex.origin.inMandelbrotSet)
    assert(MandelbrotSet contains Complex.origin)
    assert(MandelbrotSet contains Complex(0.25, 0))
    assert(MandelbrotSet contains Complex(0, 0.5))
  }

  it should "not contain these numbers" in {
    assert(!(MandelbrotSet contains Complex(1, 0)))
    assert(!(MandelbrotSet contains Complex(0, 2)))
    assert(!(MandelbrotSet contains Complex(-4, -4)))
  }

  it should "compute fewer steps for 0.3 than for 1.3" in {
    val orbit1 = MandelbrotSet.orbit(Complex(0.3, 0))
    val orbit2 = MandelbrotSet.orbit(Complex(1.3, 0))
    assert(orbit1.isInstanceOf[UnboundedOrbit])
    assert(orbit2.isInstanceOf[UnboundedOrbit])
    orbit1.points.length should be > orbit2.points.length
  }

  it should "compute orbits for numbers inside and outside" in {
    val originOrbit = MandelbrotSet.orbit(Complex.origin)
    assert(originOrbit.isInstanceOf[BoundedOrbit])

    val oneOrbit = MandelbrotSet.orbit(Complex(1, 0))
    assert(oneOrbit.isInstanceOf[UnboundedOrbit])
  }
