import java.awt.Color

import Orbits.*

trait Colorizer:

  /** Compute the color that a complex number should be visualized with.
    * @param orbit
    *   An orbit produced by repeatedly applying the Mandelbrot equation
    * @return
    *   Color to visualize this number with
    */
  def getColor(orbit: Orbit): Color

/** Simplest colorizer – paint the inside of the Mandelbrot white, everything
  * else black.
  */
object BlackWhiteColorizer extends Colorizer:

  def getColor(orbit: Orbit): Color =
    orbit.steps match
      case Some(s) => Color.BLACK
      case None    => Color.WHITE

/** Return 0 if value is smaller than lower bound, 1 if larger than upper bound,
  * or a linearly scaled value in [0, 1] if in between.
  */
def boundBy(lower: Float, upper: Float)(value: Float): Float =
  if value < lower then 0f
  else if value > upper then 1f
  else (value - lower) / (upper - lower)

/** Vary hue and intensity based on escape time. */
object RainbowColorizer extends Colorizer:

  def getColor(orbit: Orbit): Color =
    orbit.steps match
      case Some(s) =>
        // these values were hand-tweaked to look aesthetically pleasing; no meaning behind the magic numbers
        val sF = s.toFloat
        val hue = boundBy(10, 200)(sF) + .8f
        val sat = (1f - boundBy(1, 256)(sF))
        val bri = boundBy(1, 50)(sF) * .8f
        Color(Color.HSBtoRGB(hue, sat, bri))
      case None => Color.WHITE

/** Color gray if escape time is even, black otherwise. */
object BlackWhiteSteppedColorizer extends Colorizer:

  private val bounds = boundBy(1, 128)

  def getColor(orbit: Orbit): Color =
    orbit.steps match
      case Some(s) if s % 2 == 0 =>
        val bri = 0.3f + bounds(s.toFloat) * 0.6f
        Color(bri, bri, bri)
      case Some(_) => Color.BLACK
      case None    => Color.WHITE
