import scala.annotation.tailrec

object MandelbrotSet:

  /** Helper method to be able to write `c inMandelbrotSet` for `c: Complex` */
  implicit class MandelbrotComplex(c: Complex):
    def inMandelbrotSet(): Boolean =
      contains(c)

  val maxIterations = 256

  /**
   * How many steps does it take to exceed the sphere with radius 2, for f_c = z ** 2 + c applied in iteration? Returns
   * `None` if the value seems to be bounded (determined by reaching a maximum number of iterations)
   * @param c Complex number we're testing for
   * @param z Value we're iterating on
   * @return Number of steps required to exit sphere of radius 2, or None if value is bounded
   */
  def iterate(c: Complex, z: Complex=Complex(0, 0)): Option[Int] =

    @tailrec
    def iterateTCO(c: Complex, z: Complex, depth: Int): Int =
      if depth >= maxIterations then
        return Integer.MAX_VALUE

      if z.magnitude() >= 2 then
        return depth

      val zNext = z ** 2 + c
      iterateTCO(c, zNext, depth + 1)

    val steps = iterateTCO(c, z, 0)
    if steps == Integer.MAX_VALUE then None else Some(steps)

  /**
   * Is this complex number in the Mandelbrot set?
   * @param c Complex number to test for
   * @return True iff c is in M
   */
  def contains(c: Complex): Boolean =
    val steps = iterate(c)
    steps.isEmpty

