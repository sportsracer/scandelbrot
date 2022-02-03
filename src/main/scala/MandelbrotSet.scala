import scala.annotation.tailrec

/** An orbit produced by repeatedly applying the Mandelbrot equation. */
object Orbits:

  opaque type Orbit = (List[Complex], Option[Int])

  object Orbit:
    /** @param points
      *   Sequence of complex numbers produced by repeated iteration, with the
      *   original number at the rightmost position
      * @param steps
      *   Some(steps) it took for the orbit to escape the bounding sphere, or
      *   `None` if it remained bounded
      */
    def apply(points: List[Complex], steps: Option[Int]): Orbit =
      (points, steps)

  extension (o: Orbit)
    def points: List[Complex] = o._1
    def steps: Option[Int] = o._2
    def bounded: Boolean = o.steps.isEmpty

object MandelbrotSet:

  import Orbits.*

  inline val MaxIterations = 256
  inline val BoundarySquared = 2 * 2

  /** Calculate the orbit for a complex number c! */
  def orbit(c: Complex): Orbit =

    @tailrec
    def orbitTCO(orbit: List[Complex], depth: Int): Orbit =
      if depth >= MaxIterations then return Orbit(orbit, None)

      val z = orbit.head
      if z.magnitudeSquared >= BoundarySquared then
        return Orbit(orbit, Some(depth))

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
    orbit.bounded

/** Helper method to be able to write `c inMandelbrotSet` for `c: Complex` */
extension (c: Complex)
  def inMandelbrotSet: Boolean =
    MandelbrotSet.contains(c)
