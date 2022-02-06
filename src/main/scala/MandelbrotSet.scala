import scala.annotation.tailrec

/** An orbit produced by repeatedly applying the Mandelbrot equation.
  * @param points
  *   Sequence of complex numbers produced by repeated iteration, with the
  *   original number at the rightmost position
  */
trait Orbit(val points: List[Complex])
case class BoundedOrbit(override val points: List[Complex])
    extends Orbit(points)

/** @param steps
  *   Number of steps it took for the orbit to escape the bounding sphere
  */
case class UnboundedOrbit(override val points: List[Complex], steps: Int)
    extends Orbit(points)

object MandelbrotSet:

  inline val MaxIterations = 256
  inline val BoundarySquared = 256.0

  /** Calculate the orbit for a complex number c! */
  def orbit(c: Complex): Orbit =

    @tailrec
    def orbitTCO(orbit: List[Complex], depth: Int): Orbit =
      if depth >= MaxIterations then return BoundedOrbit(orbit)

      val z = orbit.head
      if z.magnitudeSquared >= BoundarySquared then
        return UnboundedOrbit(orbit, depth)

      val zNext = z ** 2 + c
      orbitTCO(zNext :: orbit, depth + 1)

    orbitTCO(c :: Nil, 0)

  /** Is this complex number in the Mandelbrot set?
    * @param c
    *   Complex number to test for
    * @return
    *   True iff c is in M
    */
  infix def contains(c: Complex): Boolean =
    val orbit = this.orbit(c)
    orbit.isInstanceOf[BoundedOrbit]

/** Helper method to be able to write `c inMandelbrotSet` for `c: Complex` */
extension (c: Complex)
  def inMandelbrotSet: Boolean =
    MandelbrotSet.contains(c)
