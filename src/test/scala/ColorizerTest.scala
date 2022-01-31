import java.awt.Color

import org.scalatest.flatspec._

class ColorizerTest extends AnyFlatSpec:

  "BlackWhiteColorizer" should "color pixels inside the Mandelbrot set white, all others black" in {
    assert(BlackWhiteColorizer.getColor(None) == Color.WHITE)
    assert(BlackWhiteColorizer.getColor(Some(1)) == Color.BLACK)
    assert(BlackWhiteColorizer.getColor(Some(256)) == Color.BLACK)
  }

  "boundBy" should "scale values to the interval [0, 1]" in {
    val bounds = boundBy(0f, 100f)

    assert(bounds(-1f) == 0f)
    assert(bounds(0f) == 0f)
    assert(bounds(50f) == .5f)
    assert(bounds(100f) == 1f)
    assert(bounds(200f) == 1f)
  }
