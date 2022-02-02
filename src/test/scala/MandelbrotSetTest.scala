import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers._

import Orbits.*

class MandelbrotSetTest extends AnyFlatSpec:

  "Orbit" should "detect whether it's bounded" in {
    val boundedOrbit = Orbit(Complex.origin :: Nil, None)
    val unboundedOrbit = Orbit(Complex(4, 0) :: Nil, Some(1))

    assert(!unboundedOrbit.bounded)
    assert(boundedOrbit.bounded)
  }

  "MandelbrotSet" should "contain these numbers" in {
    assert(Complex(0, 0).inMandelbrotSet)
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
    val orbit1 = MandelbrotSet.orbit(Complex(0.3, 0))
    val orbit2 = MandelbrotSet.orbit(Complex(1.3, 0))
    assert(!orbit1.bounded)
    assert(!orbit2.bounded)
    orbit1.steps.get should be > orbit2.steps.get
    orbit1.points.length should be > orbit2.points.length
  }

  it should "compute orbits for numbers inside and outside" in {
    val originOrbit = MandelbrotSet.orbit(Complex.origin)
    assert(originOrbit.bounded)

    val oneOrbit = MandelbrotSet.orbit(Complex(1, 0))
    assert(!oneOrbit.bounded)
  }
