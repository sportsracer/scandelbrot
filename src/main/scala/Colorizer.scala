import java.awt.Color

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
    orbit match
      case _: UnboundedOrbit => Color.BLACK
      case _                 => Color.WHITE

/** Return 0 if value is smaller than lower bound, 1 if larger than upper bound,
  * or a linearly scaled value in [0, 1] if in between.
  */
def boundBy(lower: Float, upper: Float)(value: Float): Float =
  if value < lower then 0f
  else if value > upper then 1f
  else (value - lower) / (upper - lower)

/** Vary hue and intensity based on escape time. */
object EscapeTimeColorizer extends Colorizer:

  def getColor(orbit: Orbit): Color =
    orbit match
      case UnboundedOrbit(_, steps) =>
        // these values were hand-tweaked to look aesthetically pleasing; no meaning behind the magic numbers
        val sF = steps.toFloat
        val hue = boundBy(10, 200)(sF) + .8f
        val sat = (1f - boundBy(1, 256)(sF))
        val bri = boundBy(1, 50)(sF) * .8f
        Color.getHSBColor(hue, sat, bri)
      case _ => Color.WHITE

/** Color depending on how close a number's orbit gets to the origin. */
object OriginOrbitTrapColorizer extends Colorizer:

  val ordering =
    Ordering.fromLessThan[Complex](_.magnitudeSquared < _.magnitudeSquared)

  def getColor(orbit: Orbit): Color =
    val closest = orbit.points.min(ordering)
    val magnitudeF = closest.magnitude.toFloat
    val r = 1f - boundBy(0f, .5f)(magnitudeF)
    val g = 1f - boundBy(0f, 2f)(magnitudeF)
    val b = 1f - boundBy(0f, 4f)(magnitudeF)
    Color(r, g, b)

/** Color a point depending on the quadrant that its orbit escapes to. */
object QuadrantOrbitTrapColorizer extends Colorizer:

  // Every quadrant (top left, top right …) gets a base color
  private def quadrantHue(orbit: Orbit): Float =
    val point = orbit.points.head
    (point.re >= 0, point.im >= 0) match
      case (true, true)   => 0f
      case (true, false)  => .08f
      case (false, true)  => .16f
      case (false, false) => .24f

  // … and we vary this color depending on escape time
  private def stepsHue(orbit: Orbit): Float =
    orbit match
      case UnboundedOrbit(_, steps) => steps * .27f
      case _                        => 0f

  def getColor(orbit: Orbit): Color =
    val hue = quadrantHue(orbit) + stepsHue(orbit)
    Color.getHSBColor(hue, 1f, 1f)

/** Color a point depending on how close its orbit gets to the real or imaginary
  * axis.
  */
object PickoverStalksColorizer extends Colorizer:
  /** Get the real and imaginary components closest to zero from the passed list
    * of points.
    */
  private def getClosestCoords(points: List[Complex]): (Double, Double) =
    points
      .map(c => (math.abs(c.re), math.abs(c.im)))
      .reduce((c1, c2) => (c1._1 min c2._1, c1._2 min c2._2))

  def getColor(orbit: Orbit): Color =
    val (reMin, imMin) = getClosestCoords(orbit.points)
    val coordMin = reMin min imMin

    Color.getHSBColor(
      (reMin + imMin).toFloat / 5f + .1f,
      1f,
      1f - boundBy(0f, .05f)(coordMin.toFloat)
    )
